package gt.edu.umg.controller;

import gt.edu.umg.core.entities.Usuario;
import gt.edu.umg.core.entities.Dtos.Request.LoginRequestDto;
import gt.edu.umg.core.entities.Dtos.Response.LoginResponseDto;
import gt.edu.umg.dao.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider,
                           UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Obtenemos el usuario real de la base de datos
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        // Roles REALES del usuario (no los permisos/authorities)
        Set<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getNombre())
                .collect(Collectors.toSet());

        Set<String> permisos = usuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .map(permiso -> permiso.getNombre())
        .collect(Collectors.toSet());

        return new LoginResponseDto(
    token,
    "Bearer",
    usuario.getId(),
    usuario.getUsername(),
    usuario.getEmail(),
    roles,
    permisos,   // ← nuevo, en la misma posición que en el record
    System.currentTimeMillis() + 86400000
);
    }
}