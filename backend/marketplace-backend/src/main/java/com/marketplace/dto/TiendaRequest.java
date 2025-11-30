package com.marketplace.dto;

public class TiendaRequest {

    private Integer idVendedor;   // lo mandará el frontend (id del usuario vendedor)
    private String nombre;
    private String descripcion;
    private String ruc;           // puede venir vacío o null si aún no desea formalizar

    public Integer getIdVendedor() { return idVendedor; }
    public void setIdVendedor(Integer idVendedor) { this.idVendedor = idVendedor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
}
