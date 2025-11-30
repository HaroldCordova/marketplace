package com.marketplace.dto;

public class ActualizarPagoEstadoRequest {

    // true = marcar confirmación del comprador
    private Boolean confirmarComprador;

    // true = marcar confirmación del vendedor
    private Boolean confirmarVendedor;

    // true = el admin fuerza la liberación del pago
    private Boolean liberarAdmin;

    public Boolean getConfirmarComprador() {
        return confirmarComprador;
    }

    public void setConfirmarComprador(Boolean confirmarComprador) {
        this.confirmarComprador = confirmarComprador;
    }

    public Boolean getConfirmarVendedor() {
        return confirmarVendedor;
    }

    public void setConfirmarVendedor(Boolean confirmarVendedor) {
        this.confirmarVendedor = confirmarVendedor;
    }

    public Boolean getLiberarAdmin() {
        return liberarAdmin;
    }

    public void setLiberarAdmin(Boolean liberarAdmin) {
        this.liberarAdmin = liberarAdmin;
    }
}
