package com.github.Sangarru11.Model.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "producto", schema = "netstock")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Lob
    @Column(name = "foto", columnDefinition="LONGBLOB") // <-- Modifica esta línea
    private byte[] foto;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_categoria")
    private Categoria idCategoria;

    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @ColumnDefault("0")
    @Column(name = "cantidad_disponible")
    private Integer cantidadDisponible;

    @Column(name = "codigo_interno", length = 50)
    private String codigoInterno;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_proveedor")
    private Proveedor idProveedor;

    @OneToMany(mappedBy = "idProducto")
    private Set<MovimientoProducto> movimientoProductos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idProducto")
    private Set<ProductoZona> productoZonas = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedor idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Set<MovimientoProducto> getMovimientoProductos() {
        return movimientoProductos;
    }

    public void setMovimientoProductos(Set<MovimientoProducto> movimientoProductos) {
        this.movimientoProductos = movimientoProductos;
    }

    public Set<ProductoZona> getProductoZonas() {
        return productoZonas;
    }

    public void setProductoZonas(Set<ProductoZona> productoZonas) {
        this.productoZonas = productoZonas;
    }

}