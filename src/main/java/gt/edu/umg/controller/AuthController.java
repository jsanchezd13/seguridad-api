package gt.edu.umg.controller;

import gt.edu.umg.core.entities.Usuario;
import gt.edu.umg.core.entities.Dtos.Request.LoginRequestDto;
import gt.edu.umg.core.entities.Dtos.Response.LoginResponseDto;
import gt.edu.umg.dao.UsuarioRepository;
import gt.edu.umg.security.JwtTokenProvider;
import gt.edu.umg.service.TurnstileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints de autenticación")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final TurnstileService turnstileService;
    private final UsuarioRepository usuarioRepository; 

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          TurnstileService turnstileService,
                          UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.turnstileService = turnstileService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request, HttpServletRequest httpRequest) {
        
        // 1. VALIDACIÓN DEL TURNSTILE (ACTIVADO)
        String clientIp = httpRequest.getRemoteAddr();
        boolean isHuman = turnstileService.validateToken(request.turnstileToken(), clientIp);
        
        if (!isHuman) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Verificación de seguridad fallida. Intenta de nuevo."));
        }

        // 2. BUSCAR USUARIO Y REVISAR BLOQUEO
        Usuario usuario = usuarioRepository.findByUsername(request.username()).orElse(null);

        // La forma segura de evaluar un booleano que podría ser null
        boolean estaBloqueado = usuario != null && Boolean.TRUE.equals(usuario.getCuentaBloqueada());

        if (estaBloqueado && usuario.getFechaDesbloqueo() != null) {
            if (LocalDateTime.now().isAfter(usuario.getFechaDesbloqueo())) {
                // El bloqueo ya expiró (pasaron los 15 min)
                usuario.setCuentaBloqueada(false);
                usuario.setIntentosFallidos(0);
                usuario.setFechaDesbloqueo(null);
                usuarioRepository.save(usuario);
            } else {
                // Aún está bloqueado
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body(Map.of("error", "Cuenta bloqueada por múltiples intentos fallidos. Espera 15 minutos."));
            }
        }

        // 3. SI ES HUMANO Y NO ESTÁ BLOQUEADO, PROCESAMOS EL LOGIN NORMAL
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = tokenProvider.generateToken(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            // Obtenemos el usuario real de la base de datos
            Usuario usuarioAutenticado = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

            // 4. LOGIN EXITOSO: RESETEAR CONTADORES
            usuarioAutenticado.setIntentosFallidos(0);
            usuarioAutenticado.setCuentaBloqueada(false);
            usuarioAutenticado.setFechaDesbloqueo(null);
            usuarioRepository.save(usuarioAutenticado);

            // Roles REALES del usuario
            Set<String> roles = usuarioAutenticado.getRoles().stream()
                    .map(rol -> rol.getNombre())
                    .collect(Collectors.toSet());

            Set<String> permisos = usuarioAutenticado.getRoles().stream()
                    .flatMap(rol -> rol.getPermisos().stream())
                    .map(permiso -> permiso.getNombre())
                    .collect(Collectors.toSet());

            LoginResponseDto response = new LoginResponseDto(
                token,
                "Bearer",
                usuarioAutenticado.getId(),
                usuarioAutenticado.getUsername(),
                usuarioAutenticado.getEmail(),
                roles,
                permisos,
                System.currentTimeMillis() + 86400000
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            // 5. CREDENCIALES INCORRECTAS: INCREMENTAR INTENTOS
            if (usuario != null) {
                int intentosActuales = usuario.getIntentosFallidos() != null ? usuario.getIntentosFallidos() : 0;
                int intentos = intentosActuales + 1;
                
                usuario.setIntentosFallidos(intentos);
                
                if (intentos >= 5) {
                    // BLOQUEO DE 15 MINUTOS
                    usuario.setCuentaBloqueada(true);
                    usuario.setFechaDesbloqueo(LocalDateTime.now().plusMinutes(15));
                }
                
                // Forzamos el guardado inmediato en la BD
                usuarioRepository.saveAndFlush(usuario); 

                if (intentos >= 5) {
                    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                            .body(Map.of("error", "Demasiados intentos fallidos. Cuenta bloqueada por 15 minutos."));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("error", "Usuario o contraseña incorrectos. Intento " + intentos + " de 5."));
                }
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuario o contraseña incorrectos."));
        }
    }
}