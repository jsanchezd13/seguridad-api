package gt.edu.umg.controller;

import gt.edu.umg.core.entities.Usuario;
import gt.edu.umg.core.entities.Dtos.Request.LoginRequestDto;
import gt.edu.umg.core.entities.Dtos.Response.LoginResponseDto;
import gt.edu.umg.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints de autenticación")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String token = tokenProvider.generateToken(authentication);
        
        // ✅ OBTENER EL USUARIO DE FORMA CORRECTA
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        // ✅ SI NECESITAS EL ID DEL USUARIO, DEBES OBTENERLO DE LA BASE DE DATOS
        // Por ahora devolvemos un ID fijo o lo obtenemos del UserDetails
        Long usuarioId = 1L; // Temporal, después lo obtienes de tu repositorio
        String email = username + "@clinica.com"; // Temporal

        Set<String> roles = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toSet());

        return new LoginResponseDto(
            token,
            "Bearer",
            usuarioId,
            username,
            email,
            roles,
            System.currentTimeMillis() + 86400000
        );
    }
}