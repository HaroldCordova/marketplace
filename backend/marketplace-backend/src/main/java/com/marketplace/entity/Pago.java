package com.marketplace.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdPedido", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdComprador", nullable = false)
    private Usuario comprador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdVendedor", nullable = false)
    private Usuario vendedor;

    @Column(name = "MontoTotal", precision = 10, scale = 2)
    private BigDecimal montoTotal;

    // AHORA  ➜ solo lectura, la BD la calcula
    @Column(name = "Comision", precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal comision;

    // No mapear MontoVendedor como columna insertable — la BD la calcula (persisted)
    // Proveemos un getter calculado para usarlo en DTOs.
    @Column(name = "CodigoVerificacion", unique = true)
    private String codigoVerificacion;

    @Column(name = "MetodoPago")
    private String metodoPago;

    @Column(name = "FechaPago")
    private LocalDateTime fechaPago;

    @Column(name = "Estado")
    private String estado;

    @Column(name = "ConfirmadoComprador")
    private Boolean confirmadoComprador;

    @Column(name = "ConfirmadoVendedor")
    private Boolean confirmadoVendedor;

    @Column(name = "LiberadoAdmin")
    private Boolean liberadoAdmin;

    public Pago() {}

    // getters / setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Usuario getComprador() { return comprador; }
    public void setComprador(Usuario comprador) { this.comprador = comprador; }

    public Usuario getVendedor() { return vendedor; }
    public void setVendedor(Usuario vendedor) { this.vendedor = vendedor; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public BigDecimal getComision() { return comision; }
    public void setComision(BigDecimal comision) { this.comision = comision; }

    public String getCodigoVerificacion() { return codigoVerificacion; }
    public void setCodigoVerificacion(String codigoVerificacion) { this.codigoVerificacion = codigoVerificacion; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Boolean getConfirmadoComprador() { return confirmadoComprador; }
    public void setConfirmadoComprador(Boolean confirmadoComprador) { this.confirmadoComprador = confirmadoComprador; }

    public Boolean getConfirmadoVendedor() { return confirmadoVendedor; }
    public void setConfirmadoVendedor(Boolean confirmadoVendedor) { this.confirmadoVendedor = confirmadoVendedor; }

    public Boolean getLiberadoAdmin() { return liberadoAdmin; }
    public void setLiberadoAdmin(Boolean liberadoAdmin) { this.liberadoAdmin = liberadoAdmin; }

    // -----------------------
    // Getter calculado (no mapea una columna editable)
    // -----------------------
    @Transient
    public BigDecimal getMontoVendedor() {
        if (montoTotal == null) return BigDecimal.ZERO;
        if (comision == null) return montoTotal;
        return montoTotal.subtract(comision);
    }
}
