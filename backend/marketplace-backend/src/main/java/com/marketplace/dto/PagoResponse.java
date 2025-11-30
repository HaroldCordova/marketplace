package com.marketplace.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoResponse {

    private Integer id;
    private Integer pedidoId;
    private Integer compradorId;
    private Integer vendedorId;
    private BigDecimal montoTotal;
    private BigDecimal comision;
    private BigDecimal montoVendedor;
    private String codigoVerificacion;
    private String metodoPago;
    private LocalDateTime fechaPago;
    private String estado;
    private Boolean confirmadoComprador;
    private Boolean confirmadoVendedor;
    private Boolean liberadoAdmin;

    public PagoResponse() {}

    // Getters / Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }

    public Integer getCompradorId() { return compradorId; }
    public void setCompradorId(Integer compradorId) { this.compradorId = compradorId; }

    public Integer getVendedorId() { return vendedorId; }
    public void setVendedorId(Integer vendedorId) { this.vendedorId = vendedorId; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public BigDecimal getComision() { return comision; }
    public void setComision(BigDecimal comision) { this.comision = comision; }

    public BigDecimal getMontoVendedor() { return montoVendedor; }
    public void setMontoVendedor(BigDecimal montoVendedor) { this.montoVendedor = montoVendedor; }

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
    // Helper factory: convierte entidad Pago -> PagoResponse
    // -----------------------
    public static PagoResponse fromEntity(com.marketplace.entity.Pago pago) {
        if (pago == null) return null;

        PagoResponse r = new PagoResponse();
        r.setId(pago.getId());

        if (pago.getPedido() != null) {
            r.setPedidoId(pago.getPedido().getId());
        }
        if (pago.getComprador() != null) {
            r.setCompradorId(pago.getComprador().getId());
        }
        if (pago.getVendedor() != null) {
            r.setVendedorId(pago.getVendedor().getId());
        }

        // Valores seguros (evitar nulls)
        BigDecimal total = pago.getMontoTotal() != null ? pago.getMontoTotal() : BigDecimal.ZERO;
        BigDecimal comision = pago.getComision() != null ? pago.getComision() : BigDecimal.ZERO;

        r.setMontoTotal(total);
        r.setComision(comision);
        // montoVendedor = total - comision
        r.setMontoVendedor(total.subtract(comision));

        r.setCodigoVerificacion(pago.getCodigoVerificacion());
        r.setMetodoPago(pago.getMetodoPago());
        r.setFechaPago(pago.getFechaPago());
        r.setEstado(pago.getEstado());
        r.setConfirmadoComprador(pago.getConfirmadoComprador());
        r.setConfirmadoVendedor(pago.getConfirmadoVendedor());
        r.setLiberadoAdmin(pago.getLiberadoAdmin());

        return r;
    }
}
