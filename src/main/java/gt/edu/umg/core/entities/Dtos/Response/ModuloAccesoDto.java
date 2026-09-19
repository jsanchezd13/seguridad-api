package gt.edu.umg.core.entities.Dtos.Response;

public record ModuloAccesoDto(
    Long id,
    String nombre,
    String ruta,
    String icono,
    Long idModuloPadre
) {}
