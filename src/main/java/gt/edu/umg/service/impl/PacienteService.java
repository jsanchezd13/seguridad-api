package gt.edu.umg.service.impl;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import gt.edu.umg.core.entities.Paciente;

public interface PacienteService {
	Paciente guardar(Paciente paciente);
    List<Paciente> obtenerTodos();
    Paciente obtenerPorId(Long id);
    void eliminar(Long id);
    
    // Método para consultas dinámicas (Queryable)
    List<Paciente> buscarConFiltro(Specification<Paciente> spec);
}
