package com.marketplace.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Tiendas")
public class Tienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // FK a Usuarios.Id (se mantiene como valor numérico)
    @Column(name = "IdVendedor", nullable = false)
    private Integer idVendedor;

    // Relación con Usuario SOLO para lectura (no se insertará ni actualizará desde aquí)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdVendedor", insertable = false, updatable = false)
    private Usuario vendedor;

    @Column(name = "Nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "Descripcion", length = 500)
    private String descripcion;

    @Column(name = "RUC", length = 11)
    private String ruc;

    @Column(name = "Formalizada")
    private Boolean formalizada;

    @Column(name = "FechaRegistro")
    private LocalDateTime fechaRegistro;

    @Column(name = "NecesitaFormalizar", insertable = false, updatable = false)
    private Boolean necesitaFormalizar;

    // ===== Getters y Setters =====

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdVendedor() { return idVendedor; }
    public void setIdVendedor(Integer idVendedor) { this.idVendedor = idVendedor; }

    public Usuario getVendedor() { return vendedor; }
    public void setVendedor(Usuario vendedor) { this.vendedor = vendedor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }

    public Boolean getFormalizada() { return formalizada; }
    public void setFormalizada(Boolean formalizada) { this.formalizada = formalizada; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Boolean getNecesitaFormalizar() { return necesitaFormalizar; }
    public void setNecesitaFormalizar(Boolean necesitaFormalizar) { this.necesitaFormalizar = necesitaFormalizar; }
}
