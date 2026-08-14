package gt.edu.umg.core.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sistemas")
public class Sistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    @Column(length = 100)
    private String descripcion;

    @Column(name = "url_base")
    private String urlBase;

    @OneToMany(mappedBy = "sistema")
    private Set<Modulo> modulos = new HashSet<>();

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUrlBase() { return urlBase; }
    public void setUrlBase(String urlBase) { this.urlBase = urlBase; }

    public Set<Modulo> getModulos() { return modulos; }
    public void setModulos(Set<Modulo> modulos) { this.modulos = modulos; }
}