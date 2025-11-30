package com.marketplace.dto;

import java.math.BigDecimal;

public class VendedorDashboardDTO {

    private BigDecimal ventasMes;
    private long productosPublicados;   // productos de este vendedor
    private long pedidosPendientes;     // pagos retenidos
    private long enviosPendientes;      // envíos sin entregar

    public VendedorDashboardDTO() {}

    public BigDecimal getVentasMes() {
        return ventasMes;
    }

    public void setVentasMes(BigDecimal ventasMes) {
        this.ventasMes = ventasMes;
    }

    public long getProductosPublicados() {
        return productosPublicados;
    }

    public void setProductosPublicados(long productosPublicados) {
        this.productosPublicados = productosPublicados;
    }

    public long getPedidosPendientes() {
        return pedidosPendientes;
    }

    public void setPedidosPendientes(long pedidosPendientes) {
        this.pedidosPendientes = pedidosPendientes;
    }

    public long getEnviosPendientes() {
        return enviosPendientes;
    }

    public void setEnviosPendientes(long enviosPendientes) {
        this.enviosPendientes = enviosPendientes;
    }
}
