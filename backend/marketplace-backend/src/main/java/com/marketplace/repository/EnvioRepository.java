package com.marketplace.repository;

import com.marketplace.entity.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnvioRepository extends JpaRepository<Envio, Integer> {

    // ===== LO QUE YA TENÍAS =====

    // Para listar por estado (si lo quieres usar)
    List<Envio> findByEstado(String estado);

    long countByEstado(String estado);

    // Para listar envíos de un pedido (ya lo usas)
    List<Envio> findByPedido_Id(Integer pedidoId);

    // Para confirmar por código (PedidoService.confirmarEntregaPorComprador)
    Optional<Envio> findByCodigoVerificacion(String codigoVerificacion);

    // ===== NUEVO: para dashboard de VENDEDOR =====

    @Query("""
           select count(e)
           from Envio e, Pago p
           where p.pedido = e.pedido
             and p.vendedor.id = :vendedorId
             and e.estado in ('pendiente', 'en tránsito')
           """)
    long countEnviosPendientesPorVendedor(@Param("vendedorId") Integer vendedorId);
}
