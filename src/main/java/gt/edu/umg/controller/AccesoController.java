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
    @Operation(summary = "Obtener los módulos/accesos del usuario autenticado")
    public ResponseEntity<List<ModuloAccesoDto>> obtenerAccesos() {
        // El username viene del token JWT ya validado por tu JwtAuthenticationFilter
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ResponseEntity.ok(accesoService.obtenerAccesos(username));
    }
}