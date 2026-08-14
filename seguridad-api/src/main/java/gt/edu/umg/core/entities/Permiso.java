package gt.edu.umg.core.entities;

import jakarta.persistence.*;

@Entity  
@Table(name = "permisos")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    @Column(length = 100)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "modulo_id")
    private Modulo modulo;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Modulo getModulo() { return modulo; }
    public void setModulo(Modulo modulo) { this.modulo = modulo; }
}