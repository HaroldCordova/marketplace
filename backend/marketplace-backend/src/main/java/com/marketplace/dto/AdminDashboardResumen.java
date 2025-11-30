package com.marketplace.dto;

public class AdminDashboardResumen {

    private long usuariosActivos;
    private long categoriasActivas;
    private long reportesPendientes;
    private long pagosPendientes;

    // Suma del monto retenido (montoTotal - comisión)
    private double montoRetenido;

    // 💰 Comisiones totales (todas las ventas liberadas)
    private double totalComisiones;

    // 💰 Comisiones del mes actual
    private double comisionesMesActual;

    private String updatedAt;

    // GETTERS
    public long getUsuariosActivos() { return usuariosActivos; }
    public long getCategoriasActivas() { return categoriasActivas; }
    public long getReportesPendientes() { return reportesPendientes; }
    public long getPagosPendientes() { return pagosPendientes; }
    public double getMontoRetenido() { return montoRetenido; }
    public double getTotalComisiones() { return totalComisiones; }
    public double getComisionesMesActual() { return comisionesMesActual; }
    public String getUpdatedAt() { return updatedAt; }

    // SETTERS
    public void setUsuariosActivos(long usuariosActivos) { this.usuariosActivos = usuariosActivos; }
    public void setCategoriasActivas(long categoriasActivas) { this.categoriasActivas = categoriasActivas; }
    public void setReportesPendientes(long reportesPendientes) { this.reportesPendientes = reportesPendientes; }
    public void setPagosPendientes(long pagosPendientes) { this.pagosPendientes = pagosPendientes; }
    public void setMontoRetenido(double montoRetenido) { this.montoRetenido = montoRetenido; }
    public void setTotalComisiones(double totalComisiones) { this.totalComisiones = totalComisiones; }
    public void setComisionesMesActual(double comisionesMesActual) { this.comisionesMesActual = comisionesMesActual; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
