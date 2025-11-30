package com.marketplace.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {
    private Integer id;
    private Integer usuarioId;
    private LocalDateTime fecha;
    private String estado;
    private BigDecimal total;
    private List<PedidoDetalleDTO> detalles;

    // getters / setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<PedidoDetalleDTO> getDetalles() { return detalles; }
    public void setDetalles(List<PedidoDetalleDTO> detalles) { this.detalles = detalles; }
}
