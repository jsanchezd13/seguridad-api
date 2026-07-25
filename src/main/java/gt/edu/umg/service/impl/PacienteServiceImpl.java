package gt.edu.umg.service.impl;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gt.edu.umg.core.entities.Paciente;
import gt.edu.umg.dao.PacienteRepository;

@Service
public class PacienteServiceImpl implements PacienteService {

	private final PacienteRepository pacienteRepository;

	public PacienteServiceImpl(PacienteRepository pacienteRepository) {
		this.pacienteRepository = pacienteRepository;
	}
	
	@Override
	@Transactional
	public Paciente guardar(Paciente paciente) {
		// TODO Auto-generated method stubS
		return pacienteRepository.guardar(paciente);
		//return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Paciente> obtenerTodos() {
		// TODO Auto-generated method stub
		//return null;
		return pacienteRepository.obtenerTodos();
	}

	@Override
	@Transactional(readOnly = true)
	public Paciente obtenerPorId(Long id) {
		// TODO Auto-generated method stub
		//return null;
		return pacienteRepository.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el ID: " + id));
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		// TODO Auto-generated method stub
		//if (!pacienteRepository.existsById(id)) {
        //    throw new RuntimeException("No se puede eliminar. El paciente no existe con ID: " + id);
        //}
        pacienteRepository.eliminarPorId(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Paciente> buscarConFiltro(Specification<Paciente> spec) {
		// TODO Auto-generated method stub
		//return null;
		return pacienteRepository.findAll(spec);
	}

}
