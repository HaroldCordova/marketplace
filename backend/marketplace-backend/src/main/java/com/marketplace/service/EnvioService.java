package com.marketplace.service;

import com.marketplace.dto.ConfirmarEnvioRequest;
import com.marketplace.dto.CrearEnvioRequest;
import com.marketplace.dto.EnvioDTO;
import com.marketplace.entity.Envio;
import com.marketplace.entity.Pedido;
import com.marketplace.repository.EnvioRepository;
import com.marketplace.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final PedidoRepository pedidoRepository;

    public EnvioService(EnvioRepository envioRepository, PedidoRepository pedidoRepository) {
        this.envioRepository = envioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    // Crear registro de envío
    public EnvioDTO crearEnvio(CrearEnvioRequest request) {
        Pedido pedido = pedidoRepository.findById(request.getPedidoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pedido no encontrado."));

        Envio envio = new Envio();
        envio.setPedido(pedido);
        envio.setCodigoVerificacion(request.getCodigoVerificacion());
        envio.setFechaEnvio(LocalDateTime.now());
        envio.setEstado("pendiente"); // coincide con la BD
        envio.setConfirmadoComprador(false);
        envio.setConfirmadoVendedor(false);

        envio = envioRepository.save(envio);
        return EnvioDTO.fromEntity(envio);
    }

    // Confirmaciones de comprador / vendedor
    public EnvioDTO confirmarEnvio(Integer envioId, ConfirmarEnvioRequest request) {
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Envío no encontrado."));

        if (request.isConfirmarComprador()) {
            envio.setConfirmadoComprador(true);
        }

        if (request.isConfirmarVendedor()) {
            envio.setConfirmadoVendedor(true);
        }

        if (Boolean.TRUE.equals(envio.getConfirmadoComprador())
                && Boolean.TRUE.equals(envio.getConfirmadoVendedor())) {

            envio.setEstado("entregado");

            if (envio.getFechaEntrega() == null) {
                envio.setFechaEntrega(LocalDateTime.now());
            }
        }

        envio = envioRepository.save(envio);
        return EnvioDTO.fromEntity(envio);
    }

    // Envíos por pedido (para detalle de pedido)
    public List<EnvioDTO> listarPorPedido(Integer pedidoId) {
        return envioRepository.findByPedido_Id(pedidoId)
                .stream()
                .map(EnvioDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
