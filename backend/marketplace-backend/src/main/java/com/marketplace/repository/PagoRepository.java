package com.marketplace.repository;

import com.marketplace.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    // ===== LO QUE YA TENÍAS =====
    List<Pago> findByVendedor_Id(Integer vendedorId);

    List<Pago> findByComprador_Id(Integer compradorId);

    Optional<Pago> findByCodigoVerificacion(String codigoVerificacion);

    List<Pago> findByEstado(String estado);

    long countByEstado(String estado);

    // Suma del monto del vendedor retenido = sum(montoTotal - comision) en 'retenido'
    @Query("""
           select coalesce(sum(p.montoTotal) - sum(p.comision), 0)
           from Pago p
           where p.estado = 'retenido'
           """)
    BigDecimal sumMontoVendedorRetenido();

    // Comisiones totales (pagos liberados)
    @Query("select coalesce(sum(p.comision), 0) from Pago p where p.estado = 'liberado'")
    BigDecimal sumComisionLiberada();

    // Comisiones liberadas en un rango de fechas
    @Query("""
           select coalesce(sum(p.comision), 0)
           from Pago p
           where p.estado = 'liberado'
             and p.fechaPago between :inicio and :fin
           """)
    BigDecimal sumComisionLiberadaEnRango(@Param("inicio") LocalDateTime inicio,
                                          @Param("fin") LocalDateTime fin);

    // ===== NUEVO: para dashboard de VENDEDOR =====

    // Ventas del vendedor en un rango (lo que recibe = montoTotal - comision)
    @Query("""
           select coalesce(sum(p.montoTotal) - sum(p.comision), 0)
           from Pago p
           where p.estado = 'liberado'
             and p.vendedor.id = :vendedorId
             and p.fechaPago between :inicio and :fin
           """)
    BigDecimal sumMontoVendedorEnRango(@Param("vendedorId") Integer vendedorId,
                                       @Param("inicio") LocalDateTime inicio,
                                       @Param("fin") LocalDateTime fin);

    // Cantidad de pagos retenidos de ese vendedor
    @Query("""
           select count(p)
           from Pago p
           where p.estado = 'retenido'
             and p.vendedor.id = :vendedorId
           """)
    long countPagosPendientesPorVendedor(@Param("vendedorId") Integer vendedorId);
}
