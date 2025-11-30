package com.marketplace.dto;

import java.time.LocalDateTime;

public class TiendaResponse {

    private Integer id;
    private Integer idVendedor;
    private String nombre;
    private String descripcion;
    private String ruc;
    private Boolean formalizada;
    private Boolean necesitaFormalizar;
    private LocalDateTime fechaRegistro;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdVendedor() { return idVendedor; }
    public void setIdVendedor(Integer idVendedor) { this.idVendedor = idVendedor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public Boolean getFormalizada() { return formalizada; }
    public void setFormalizada(Boolean formalizada) { this.formalizada = formalizada; }

    public Boolean getNecesitaFormalizar() { return necesitaFormalizar; }
    public void setNecesitaFormalizar(Boolean necesitaFormalizar) { this.necesitaFormalizar = necesitaFormalizar; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
