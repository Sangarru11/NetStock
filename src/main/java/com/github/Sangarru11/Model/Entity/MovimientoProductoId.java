package com.github.Sangarru11.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MovimientoProductoId implements Serializable {
    private static final long serialVersionUID = 6110094511109651787L;
    @Column(name = "id_movimiento", nullable = false)
    private Integer idMovimiento;

    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;

    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MovimientoProductoId entity = (MovimientoProductoId) o;
        return Objects.equals(this.idMovimiento, entity.idMovimiento) &&
                Objects.equals(this.idProducto, entity.idProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMovimiento, idProducto);
    }

}