package com.marketplace.dto;

import java.time.LocalDateTime;

public class EnvioResponse {
    private Integer id;
    private Integer pedidoId;
    private String codigoVerificacion;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaEntrega;
    private String estado;
    private Boolean confirmadoComprador;
    private Boolean confirmadoVendedor;

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }

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
