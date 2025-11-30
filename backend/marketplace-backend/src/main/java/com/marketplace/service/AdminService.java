package com.marketplace.service;

import com.marketplace.dto.AdminDashboardResumen;
import com.marketplace.entity.Pago;
import com.marketplace.repository.UsuarioRepository;
import com.marketplace.repository.CategoriaRepository;
import com.marketplace.repository.PagoRepository;
import com.marketplace.repository.EnvioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final PagoRepository pagoRepository;
    private final EnvioRepository envioRepository;

    public AdminService(UsuarioRepository usuarioRepository,
                        CategoriaRepository categoriaRepository,
                        PagoRepository pagoRepository,
                        EnvioRepository envioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.pagoRepository = pagoRepository;
        this.envioRepository = envioRepository;
    }

    public AdminDashboardResumen obtenerResumen() {
        AdminDashboardResumen dto = new AdminDashboardResumen();

        // -------------------- Usuarios y categorías --------------------
        long usuariosActivos = usuarioRepository.countByEstadoTrue();
        long categoriasActivas = categoriaRepository.count();

        // -------------------- Pagos retenidos --------------------------
        List<Pago> pagosRetenidos = pagoRepository.findByEstado("retenido");
        long pagosPendientes = pagosRetenidos.size();

        // Monto retenido = Σ (montoTotal - comision) de pagos retenidos
        BigDecimal montoRetenidoBD = pagosRetenidos.stream()
                .map(p -> {
                    BigDecimal total = p.getMontoTotal() != null ? p.getMontoTotal() : BigDecimal.ZERO;
                    BigDecimal comision = p.getComision() != null ? p.getComision() : BigDecimal.ZERO;
                    return total.subtract(comision);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // -------------------- Pagos liberados (ganancias) --------------
        List<Pago> pagosLiberados = pagoRepository.findByEstado("liberado");

        // Ganancia total = Σ comision de pagos liberados
        BigDecimal totalComisionesBD = pagosLiberados.stream()
                .map(p -> p.getComision() != null ? p.getComision() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Ganancia del mes actual
        LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime ahora = LocalDateTime.now();

        BigDecimal comisionesMesBD = pagosLiberados.stream()
                .filter(p -> {
                    LocalDateTime fecha = p.getFechaPago();
                    return fecha != null && !fecha.isBefore(inicioMes) && !fecha.isAfter(ahora);
                })
                .map(p -> p.getComision() != null ? p.getComision() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // -------------------- Envíos en tránsito -----------------------
        long enviosTransito = envioRepository.countByEstado("en tránsito");

        // -------------------- Llenar DTO -------------------------------
        dto.setUsuariosActivos(usuariosActivos);
        dto.setCategoriasActivas(categoriasActivas);
        dto.setPagosPendientes(pagosPendientes);
        dto.setReportesPendientes(pagosPendientes + enviosTransito);

        dto.setMontoRetenido(montoRetenidoBD.doubleValue());
        dto.setTotalComisiones(totalComisionesBD.doubleValue());
        dto.setComisionesMesActual(comisionesMesBD.doubleValue());

        dto.setUpdatedAt(java.time.OffsetDateTime.now().toString());

        return dto;
    }
}
