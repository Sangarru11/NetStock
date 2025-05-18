package com.github.Sangarru11.Model.Entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "zona", schema = "netstock")
public class Zona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona", nullable = false)
    private Integer idZona;

    @Column(name = "nombre_zona", length = 100)
    private String nombreZona;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @OneToMany(mappedBy = "idZona")
    private Set<ProductoZona> productoZonas = new LinkedHashSet<>();

    public Integer getId() {
        return idZona;
    }

    public void setId(Integer id) {
        this.idZona = id;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<ProductoZona> getProductoZonas() {
        return productoZonas;
    }

    public void setProductoZonas(Set<ProductoZona> productoZonas) {
        this.productoZonas = productoZonas;
    }

}