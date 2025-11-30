package com.marketplace.dto;

public class ConfirmarEnvioRequest {

    private boolean confirmarComprador;
    private boolean confirmarVendedor;

    public boolean isConfirmarComprador() {
        return confirmarComprador;
    }

    public void setConfirmarComprador(boolean confirmarComprador) {
        this.confirmarComprador = confirmarComprador;
    }

    public boolean isConfirmarVendedor() {
        return confirmarVendedor;
    }

    public void setConfirmarVendedor(boolean confirmarVendedor) {
        this.confirmarVendedor = confirmarVendedor;
    }
}