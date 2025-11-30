package com.marketplace.service;

import com.marketplace.dto.VendedorDashboardDTO;
import com.marketplace.repository.EnvioRepository;
import com.marketplace.repository.PagoRepository;
import com.marketplace.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class VendedorService {

    private final PagoRepository pagoRepository;
    private final ProductoRepository productoRepository;
    private final EnvioRepository envioRepository;

    public VendedorService(PagoRepository pagoRepository,
                           ProductoRepository productoRepository,
                           EnvioRepository envioRepository) {
        this.pagoRepository = pagoRepository;
        this.productoRepository = productoRepository;
        this.envioRepository = envioRepository;
    }

    public VendedorDashboardDTO obtenerDashboard(Integer idVendedor) {
        VendedorDashboardDTO dto = new VendedorDashboardDTO();

        // rango del mes actual
        LocalDateTime inicioMes = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime finMes = LocalDateTime.now();

        // ventas del mes (lo que recibe el vendedor)
        BigDecimal ventasMes = pagoRepository.sumMontoVendedorEnRango(idVendedor, inicioMes, finMes);
        dto.setVentasMes(ventasMes != null ? ventasMes : BigDecimal.ZERO);

        // productos publicados por ese vendedor
        long productos = productoRepository.countByVendedor_Id(idVendedor);
        dto.setProductosPublicados(productos);

        // pagos retenidos (pendientes de liberar)
        long pagosPendientes = pagoRepository.countPagosPendientesPorVendedor(idVendedor);
        dto.setPedidosPendientes(pagosPendientes);

        // envíos pendientes o en tránsito
        long enviosPendientes = envioRepository.countEnviosPendientesPorVendedor(idVendedor);
        dto.setEnviosPendientes(enviosPendientes);

        return dto;
    }
}
