package gt.edu.umg.dao;

import gt.edu.umg.core.entities.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuloRepository extends JpaRepository<Modulo, Long> {

    @Query("""
        SELECT DISTINCT m FROM Modulo m
        JOIN Permiso p ON p.modulo.id = m.id
        JOIN Rol r JOIN r.permisos rp ON rp.id = p.id
        JOIN Usuario u JOIN u.roles ur ON ur.id = r.id
        WHERE u.username = :username AND m.activo = true
        ORDER BY m.id
    """)
    List<Modulo> buscarModulosPorUsuario(@Param("username") String username);
}