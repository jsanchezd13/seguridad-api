package gt.edu.umg.core.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "modulos")
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    @Column(length = 100)
    private String descripcion;

    @Column(name = "ruta")
    private String ruta;

    @ManyToOne
    @JoinColumn(name = "sistema_id")
    private Sistema sistema;

    @OneToMany(mappedBy = "modulo")
    private Set<Permiso> permisos = new HashSet<>();

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }

    public Sistema getSistema() { return sistema; }
    public void setSistema(Sistema sistema) { this.sistema = sistema; }

    public Set<Permiso> getPermisos() { return permisos; }
    public void setPermisos(Set<Permiso> permisos) { this.permisos = permisos; }
}