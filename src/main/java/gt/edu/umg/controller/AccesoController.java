package gt.edu.umg.controller;

import gt.edu.umg.core.entities.Dtos.Response.ModuloAccesoDto;
import gt.edu.umg.service.impl.AccesoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; 
import java.util.List;

@RestController
@RequestMapping("/auth")
@Tag(name = "Accesos", description = "Endpoint de accesos/menú dinámico del usuario autenticado")
public class AccesoController {

    private final AccesoService accesoService;

    public AccesoController(AccesoService accesoService) {
        this.accesoService = accesoService;
    }

    @GetMapping("/accesos")
@Operation(
    summary = "Obtener los módulos/accesos del usuario autenticado"
)
@SecurityRequirement(name = "bearerAuth")
public ResponseEntity<List<ModuloAccesoDto>> obtenerAccesos() {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    System.out.println("========================================");
    System.out.println("Authentication: " + authentication);
    System.out.println("Nombre: " +
            (authentication != null ? authentication.getName() : "null"));
    System.out.println("========================================");

    if (authentication == null ||
        !authentication.isAuthenticated() ||
        authentication.getPrincipal().equals("anonymousUser")) {

        return ResponseEntity.status(401).build();
    }

    String username = authentication.getName();

    System.out.println("Buscando accesos para: [" + username + "]");

    List<ModuloAccesoDto> accesos =
            accesoService.obtenerAccesos(username);

    System.out.println("Total accesos: " + accesos.size());

    return ResponseEntity.ok(accesos);
}
}