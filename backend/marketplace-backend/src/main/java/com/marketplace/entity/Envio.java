package com.marketplace.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdPedido", nullable = false)
    private Pedido pedido;

    @Column(name = "CodigoVerificacion", unique = true)
    private String codigoVerificacion;

    @Column(name = "FechaEnvio")
    private LocalDateTime fechaEnvio;

    @Column(name = "FechaEntrega")
    private LocalDateTime fechaEntrega;

    @Column(name = "Estado")
    private String estado;

    @Column(name = "ConfirmadoComprador")
    private Boolean confirmadoComprador;

    @Column(name = "ConfirmadoVendedor")
    private Boolean confirmadoVendedor;

    public Envio() {}

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public String getCodigoVerificacion() { return codigoVerificacion; }
    public void setCodigoVerificacion(String codigoVerificacion) { this.codigoVerificacion = codigoVerificacion; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Boolean getConfirmadoComprador() { return confirmadoComprador; }
    public void setConfirmadoComprador(Boolean confirmadoComprador) { this.confirmadoComprador = confirmadoComprador; }

    public Boolean getConfirmadoVendedor() { return confirmadoVendedor; }
    public void setConfirmadoVendedor(Boolean confirmadoVendedor) { this.confirmadoVendedor = confirmadoVendedor; }
}
