package com.marketplace.dto;

import com.marketplace.entity.Envio;

public class EnvioDTO {

    private Integer id;
    private Integer pedidoId;
    private String codigoVerificacion;
    private String fechaEnvio;
    private String fechaEntrega;
    private String estado;
    private boolean confirmadoComprador;
    private boolean confirmadoVendedor;

    // --- getters y setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }

    public String getCodigoVerificacion() { return codigoVerificacion; }
    public void setCodigoVerificacion(String codigoVerificacion) { this.codigoVerificacion = codigoVerificacion; }

    public String getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(String fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isConfirmadoComprador() { return confirmadoComprador; }
    public void setConfirmadoComprador(boolean confirmadoComprador) { this.confirmadoComprador = confirmadoComprador; }

    public boolean isConfirmadoVendedor() { return confirmadoVendedor; }
    public void setConfirmadoVendedor(boolean confirmadoVendedor) { this.confirmadoVendedor = confirmadoVendedor; }

    // --- mapeo desde entity, todo con null-safe ---
    public static EnvioDTO fromEntity(Envio envio) {
        EnvioDTO dto = new EnvioDTO();
        dto.setId(envio.getId());
        dto.setPedidoId(envio.getPedido() != null ? envio.getPedido().getId() : null);
        dto.setCodigoVerificacion(envio.getCodigoVerificacion());
        dto.setFechaEnvio(envio.getFechaEnvio() != null ? envio.getFechaEnvio().toString() : null);
        dto.setFechaEntrega(envio.getFechaEntrega() != null ? envio.getFechaEntrega().toString() : null);
        dto.setEstado(envio.getEstado());
        dto.setConfirmadoComprador(Boolean.TRUE.equals(envio.getConfirmadoComprador()));
        dto.setConfirmadoVendedor(Boolean.TRUE.equals(envio.getConfirmadoVendedor()));
        return dto;
    }
}
