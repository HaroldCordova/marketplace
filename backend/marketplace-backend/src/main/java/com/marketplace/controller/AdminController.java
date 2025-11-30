package com.marketplace.controller;

import com.marketplace.dto.AdminDashboardResumen;
import com.marketplace.dto.EnvioDTO;
import com.marketplace.dto.FormalizacionPanelDTO;
import com.marketplace.dto.PagoResponse;
import com.marketplace.entity.Envio;
import com.marketplace.entity.Pago;
import com.marketplace.entity.Tienda;
import com.marketplace.repository.CategoriaRepository;
import com.marketplace.repository.EnvioRepository;
import com.marketplace.repository.PagoRepository;
import com.marketplace.repository.TiendaRepository;
import com.marketplace.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final PagoRepository pagoRepository;
    private final EnvioRepository envioRepository;
    private final TiendaRepository tiendaRepository;

    public AdminController(UsuarioRepository usuarioRepository,
                           CategoriaRepository categoriaRepository,
                           PagoRepository pagoRepository,
                           EnvioRepository envioRepository,
                           TiendaRepository tiendaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.pagoRepository = pagoRepository;
        this.envioRepository = envioRepository;
        this.tiendaRepository = tiendaRepository;
    }

    // ---------- RESUMEN PARA LAS TARJETAS DEL DASHBOARD ----------
    @GetMapping("/resumen")
    public AdminDashboardResumen obtenerResumen() {

        AdminDashboardResumen dto = new AdminDashboardResumen();

        // Usuarios y categorías
        long usuariosActivos = usuarioRepository.countByEstadoTrue();
        long categoriasActivas = categoriaRepository.count();

        // PAGOS RETENIDOS
        List<Pago> pagosRetenidosList = pagoRepository.findByEstado("retenido");
        long pagosRetenidos = pagosRetenidosList.size();

        // Monto retenido = sum( montoTotal - comision ) de los retenidos
        BigDecimal montoRetenidoBD = pagosRetenidosList.stream()
                .map(p -> {
                    BigDecimal total = p.getMontoTotal() != null ? p.getMontoTotal() : BigDecimal.ZERO;
                    BigDecimal comision = p.getComision() != null ? p.getComision() : BigDecimal.ZERO;
                    return total.subtract(comision);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // PAGOS LIBERADOS (para ganancias)
        List<Pago> pagosLiberados = pagoRepository.findByEstado("liberado");

        // Ganancia total = sum(comision) de todos los liberados
        BigDecimal totalComisionesBD = pagosLiberados.stream()
                .map(p -> p.getComision() != null ? p.getComision() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Ganancia mes actual
        LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime ahora = LocalDateTime.now();

        BigDecimal comisionesMesBD = pagosLiberados.stream()
                .filter(p -> {
                    LocalDateTime fecha = p.getFechaPago();
                    return fecha != null && !fecha.isBefore(inicioMes) && !fecha.isAfter(ahora);
                })
                .map(p -> p.getComision() != null ? p.getComision() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Envíos pendientes (según tu PedidoService creas envíos con estado "pendiente")
        long enviosPendientes = envioRepository.countByEstado("pendiente");

        // Llenar DTO
        dto.setUsuariosActivos(usuariosActivos);
        dto.setCategoriasActivas(categoriasActivas);
        dto.setReportesPendientes(pagosRetenidos + enviosPendientes);
        dto.setPagosPendientes(pagosRetenidos);
        dto.setMontoRetenido(montoRetenidoBD.doubleValue());
        dto.setTotalComisiones(totalComisionesBD.doubleValue());
        dto.setComisionesMesActual(comisionesMesBD.doubleValue());
        dto.setUpdatedAt(java.time.OffsetDateTime.now().toString());

        return dto;
    }

    // ---------- PAGOS RETENIDOS (TABLA) ----------
    @GetMapping("/pagos/retenidos")
    public List<PagoResponse> listarPagosRetenidos() {
        List<Pago> pagos = pagoRepository.findByEstado("retenido");
        return pagos.stream()
                .map(PagoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // ---------- LIBERAR PAGO MANUALMENTE ----------
    @PutMapping("/pagos/{id}/liberar")
    public ResponseEntity<Void> liberarPago(@PathVariable Integer id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setEstado("liberado");
        pago.setLiberadoAdmin(true);
        pagoRepository.save(pago);

        return ResponseEntity.noContent().build();
    }

    // ---------- ENVIOS PARA EL PANEL (TABLA DE ENVIOS) ----------
    @GetMapping("/envios/pendientes")
    public List<EnvioDTO> listarEnviosPendientes() {
        // puedes filtrar por estado si quieres solo "pendiente"
        List<Envio> envios = envioRepository.findByEstado("pendiente");
        // o si quieres ver todos los envíos:
        // List<Envio> envios = envioRepository.findAll();
        return envios.stream()
                .map(EnvioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ---------- FORMALIZACIÓN TIENDAS ----------
    @GetMapping("/tiendas-formalizacion")
    public List<FormalizacionPanelDTO> listarTiendasFormalizacion() {
        List<Tienda> tiendas = tiendaRepository.findAll();
        return tiendas.stream()
                .map(FormalizacionPanelDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/panel")
    public String panel() {
        return "Bienvenido al panel ADMIN del marketplace.";
    }
}
