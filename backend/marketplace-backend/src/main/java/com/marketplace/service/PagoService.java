package com.marketplace.service;

import com.marketplace.dto.ActualizarPagoEstadoRequest;
import com.marketplace.dto.CrearPagoRequest;
import com.marketplace.dto.PagoResponse;
import com.marketplace.entity.Pago;
import com.marketplace.entity.Pedido;
import com.marketplace.entity.Usuario;
import com.marketplace.repository.PagoRepository;
import com.marketplace.repository.PedidoRepository;
import com.marketplace.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    public PagoService(
            PagoRepository pagoRepository,
            PedidoRepository pedidoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.pagoRepository = pagoRepository;
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public PagoResponse crearPago(CrearPagoRequest request) {
        Pedido pedido = pedidoRepository.findById(request.getPedidoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado."));

        Usuario comprador = usuarioRepository.findById(request.getCompradorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comprador no encontrado."));

        Usuario vendedor = usuarioRepository.findById(request.getVendedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendedor no encontrado."));

        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setComprador(comprador);
        pago.setVendedor(vendedor);
        pago.setMontoTotal(request.getMontoTotal());
        // comision la calcula la BD (5% de MontoTotal)
        pago.setCodigoVerificacion(request.getCodigoVerificacion());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("retenido");
        pago.setConfirmadoComprador(false);
        pago.setConfirmadoVendedor(false);
        pago.setLiberadoAdmin(false);

        pago = pagoRepository.save(pago);
        return mapToResponse(pago);
    }

    public List<PagoResponse> listarPagosVendedor(Integer vendedorId) {
        return pagoRepository.findByVendedor_Id(vendedorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PagoResponse actualizarEstado(Integer pagoId, ActualizarPagoEstadoRequest request) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado."));

        if (request.getConfirmarComprador() != null && request.getConfirmarComprador()) {
            pago.setConfirmadoComprador(true);
        }

        if (request.getConfirmarVendedor() != null && request.getConfirmarVendedor()) {
            pago.setConfirmadoVendedor(true);
        }

        if (request.getLiberarAdmin() != null && request.getLiberarAdmin()) {
            pago.setLiberadoAdmin(true);
            pago.setEstado("liberado");
        }

        pago = pagoRepository.save(pago);
        return mapToResponse(pago);
    }

    private PagoResponse mapToResponse(Pago pago) {
        PagoResponse dto = new PagoResponse();
        dto.setId(pago.getId());
        dto.setPedidoId(pago.getPedido().getId());
        dto.setCompradorId(pago.getComprador().getId());
        dto.setVendedorId(pago.getVendedor() != null ? pago.getVendedor().getId() : null);
        dto.setMontoTotal(pago.getMontoTotal());
        dto.setComision(pago.getComision());
        dto.setMontoVendedor(pago.getMontoVendedor());
        dto.setCodigoVerificacion(pago.getCodigoVerificacion());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setFechaPago(pago.getFechaPago());
        dto.setEstado(pago.getEstado());
        dto.setConfirmadoComprador(pago.getConfirmadoComprador());
        dto.setConfirmadoVendedor(pago.getConfirmadoVendedor());
        dto.setLiberadoAdmin(pago.getLiberadoAdmin());
        return dto;
    }
}
