package gt.edu.umg.core.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "modulos")
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(length = 100)
    private String descripcion;

    @Column(length = 255)
    private String ruta;

    @Column(length = 50)
    private String icono;

    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modulo_padre")
    private Modulo moduloPadre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sistema_id")
    private Sistema sistema;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Modulo getModuloPadre() { return moduloPadre; }
    public void setModuloPadre(Modulo moduloPadre) { this.moduloPadre = moduloPadre; }

    public Sistema getSistema() { return sistema; }
    public void setSistema(Sistema sistema) { this.sistema = sistema; }
}