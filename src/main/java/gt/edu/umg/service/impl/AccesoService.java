package gt.edu.umg.service.impl;

import gt.edu.umg.core.entities.Modulo;
import gt.edu.umg.core.entities.Dtos.Response.ModuloAccesoDto;
import gt.edu.umg.dao.ModuloRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccesoService {

    private final ModuloRepository moduloRepository;

    public AccesoService(ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    public List<ModuloAccesoDto> obtenerAccesos(String username) {
        List<Modulo> modulos = moduloRepository.buscarModulosPorUsuario(username);

        // Incluye automáticamente los módulos padre, aunque no tengan permiso directo
        Map<Long, Modulo> resultado = new LinkedHashMap<>();
        for (Modulo m : modulos) {
            resultado.put(m.getId(), m);
            Modulo padre = m.getModuloPadre();
            while (padre != null && !resultado.containsKey(padre.getId())) {
                resultado.put(padre.getId(), padre);
                padre = padre.getModuloPadre();
            }
        }

        return resultado.values().stream()
                .map(m -> new ModuloAccesoDto(
                        m.getId(),
                        m.getNombre(),
                        m.getRuta(),
                        m.getIcono(),
                        m.getModuloPadre() != null ? m.getModuloPadre().getId() : null
                ))
                .toList();
    }
}