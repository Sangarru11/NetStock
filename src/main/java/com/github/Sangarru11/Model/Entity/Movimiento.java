package com.github.Sangarru11.Model.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "movimiento", schema = "netstock")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento", nullable = false)
    private Integer idMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;

    @Lob
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @ColumnDefault("current_timestamp()")
    @Column(name = "fecha")
    private Instant fecha;

    @Lob
    @Column(name = "observaciones")
    private String observaciones;

    @OneToMany(mappedBy = "idMovimiento")
    private Set<MovimientoProducto> movimientoProductos = new LinkedHashSet<>();

    public Integer getId() {
        return idMovimiento;
    }

    public void setId(Integer id) {
        this.idMovimiento = id;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Set<MovimientoProducto> getMovimientoProductos() {
        return movimientoProductos;
    }

    public void setMovimientoProductos(Set<MovimientoProducto> movimientoProductos) {
        this.movimientoProductos = movimientoProductos;
    }

}