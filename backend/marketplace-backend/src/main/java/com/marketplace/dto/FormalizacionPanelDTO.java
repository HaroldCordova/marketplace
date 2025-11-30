package com.marketplace.dto;

import java.time.LocalDateTime;

import com.marketplace.entity.Tienda;
import com.marketplace.entity.Usuario;

public class FormalizacionPanelDTO {

    private Integer id;
    private String nombreTienda;
    private String vendedor;
    private String correo;
    private String ruc;
    private String estadoFormalizacion;
    private LocalDateTime fechaRegistro;

    public static FormalizacionPanelDTO fromEntity(Tienda tienda) {
        FormalizacionPanelDTO dto = new FormalizacionPanelDTO();
        dto.setId(tienda.getId());
        dto.setNombreTienda(tienda.getNombre());

        Usuario vendedor = tienda.getVendedor();
        if (vendedor != null) {
            dto.setVendedor(vendedor.getNombre() + " " + vendedor.getApellido());
            dto.setCorreo(vendedor.getCorreo());
        }

        dto.setRuc(tienda.getRuc());

        if (tienda.getRuc() == null || tienda.getRuc().trim().isEmpty()) {
            dto.setEstadoFormalizacion("No formalizado");
        } else {
            dto.setEstadoFormalizacion("Formalizado con RUC");
        }

        dto.setFechaRegistro(tienda.getFechaRegistro());
        return dto;
    }

    // Getters y setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getEstadoFormalizacion() {
        return estadoFormalizacion;
    }

    public void setEstadoFormalizacion(String estadoFormalizacion) {
        this.estadoFormalizacion = estadoFormalizacion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
