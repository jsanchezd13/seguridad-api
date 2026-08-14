package gt.edu.umg.core.entities.Dtos.Response;

import java.util.Set;

public record LoginResponseDto(
    
    String token,
    String tipo,
    Long usuarioId,
    String username,
    String email,
    Set<String> roles,
    Long expiracion

) {}