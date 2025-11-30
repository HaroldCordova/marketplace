package com.marketplace.service;

import com.marketplace.dto.*;
import com.marketplace.entity.*;
import com.marketplace.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EnvioRepository envioRepository;
    private final PagoRepository pagoRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            DetallePedidoRepository detallePedidoRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository,
            EnvioRepository envioRepository,
            PagoRepository pagoRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.envioRepository = envioRepository;
        this.pagoRepository = pagoRepository;
    }

    private Usuario getCurrentUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }
        String correo = auth.getName(); // JWT usa 'username' = correo
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario autenticado no encontrado"));
    }

    @Transactional
    public PedidoResponse crearPedido(CrearPedidoRequest request) {
        // verifica usuario: si el request.usuarioId es distinto del usuario autenticado, rechazar
        Usuario comprador = getCurrentUsuario();

        if (!Objects.equals(comprador.getId(), request.getUsuarioId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes crear un pedido para otro usuario");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido sin items");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(comprador);
        pedido.setEstado("pendiente");
        pedido.setFecha(LocalDateTime.now());
        pedido.setTotal(BigDecimal.ZERO);
        pedido = pedidoRepository.save(pedido);

        BigDecimal total = BigDecimal.ZERO;
        List<DetallePedido> detallesGuardados = new ArrayList<>();

        for (CrearPedidoRequest.ItemCarrito it : request.getItems()) {
            Producto producto = productoRepository.findById(it.getProductoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado: " + it.getProductoId()));

            if (producto.getStock() < it.getCantidad()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock insuficiente para producto: " + producto.getNombre());
            }

            BigDecimal precio = producto.getPrecio();
            BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(it.getCantidad()));

            DetallePedido det = new DetallePedido();
            det.setPedido(pedido);
            det.setProducto(producto);
            det.setCantidad(it.getCantidad());
            det.setSubtotal(subtotal);

            detallePedidoRepository.save(det);
            detallesGuardados.add(det);

            // actualizar stock
            producto.setStock(producto.getStock() - it.getCantidad());
            productoRepository.save(producto);

            total = total.add(subtotal);
        }

        pedido.setTotal(total);
        pedido.setDetalles(detallesGuardados);
        pedido = pedidoRepository.save(pedido);

        // crear envio y pago básicos (pendientes) con codigo de verificacion
        Envio envio = new Envio();
        envio.setPedido(pedido);
        envio.setCodigoVerificacion(generateCodigoVerificacion());
        envio.setFechaEnvio(LocalDateTime.now());
        envio.setEstado("pendiente");
        envio.setConfirmadoComprador(false);
        envio.setConfirmadoVendedor(false);
        envioRepository.save(envio);

        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setComprador(comprador);
        // determinar vendedor: si pedido contiene productos de varios vendedores, esto se puede adaptar
        // por simplicidad se asigna al vendedor del primer producto
        Usuario vendedor = detallesGuardados.get(0).getProducto().getIdVendedor() != null
                ? usuarioRepository.findById(detallesGuardados.get(0).getProducto().getIdVendedor()).orElse(null)
                : null;
        if (vendedor != null) {
            pago.setVendedor(vendedor);
        }
        pago.setMontoTotal(total);                // <- solo monto total
        // comision la calcula la BD (columna calculada)
        pago.setCodigoVerificacion(envio.getCodigoVerificacion());
        pago.setMetodoPago("tarjeta");
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("retenido");
        pago.setConfirmadoComprador(false);
        pago.setConfirmadoVendedor(false);
        pago.setLiberadoAdmin(false);
        pagoRepository.save(pago);

        return mapToResponse(pedido);
    }

    public List<PedidoResponse> listarPedidosPorUsuario(Integer usuarioId) {
        // validar que usuario autenticado es el mismo o es ADMIN
        Usuario current = getCurrentUsuario();
        if (!Objects.equals(current.getId(), usuarioId) && !"admin".equalsIgnoreCase(current.getRol())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado a ver pedidos de otro usuario");
        }

        return pedidoRepository.findByUsuario_Id(usuarioId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PedidoResponse obtenerPedido(Integer pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        // validaciones de seguridad: solo comprador o admin
        Usuario current = getCurrentUsuario();
        if (!Objects.equals(current.getId(), pedido.getUsuario().getId()) && !"admin".equalsIgnoreCase(current.getRol())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
        return mapToResponse(pedido);
    }

    @Transactional
    public EnvioResponse confirmarEntregaPorComprador(String codigoVerificacion) {
        Envio envio = envioRepository.findByCodigoVerificacion(codigoVerificacion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Envio no encontrado"));
        Usuario current = getCurrentUsuario();
        if (!Objects.equals(current.getId(), envio.getPedido().getUsuario().getId()) && !"admin".equalsIgnoreCase(current.getRol())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
        envio.setConfirmadoComprador(true);
        envio.setEstado("entregado");
        envio.setFechaEntrega(LocalDateTime.now());
        envioRepository.save(envio);

        // el trigger en DB liberará el pago cuando vendedor también confirme
        return mapToResponse(envio);
    }

    @Transactional
    public PagoResponse confirmarPagoPorVendedor(String codigoPago) {
        Pago pago = pagoRepository.findByCodigoVerificacion(codigoPago)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));
        Usuario current = getCurrentUsuario();
        // vendedor confirma solo si es el vendedor del pago o admin
        if (!Objects.equals(current.getId(), pago.getVendedor().getId()) && !"admin".equalsIgnoreCase(current.getRol())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
        }
        pago.setConfirmadoVendedor(true);
        pago.setEstado("confirmado_vendedor");
        pagoRepository.save(pago);
        return mapToResponse(pago);
    }

    private String generateCodigoVerificacion() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 18).toUpperCase();
    }

    // mappers
    private PedidoResponse mapToResponse(Pedido pedido) {
        PedidoResponse dto = new PedidoResponse();
        dto.setId(pedido.getId());
        dto.setUsuarioId(pedido.getUsuario().getId());
        dto.setFecha(pedido.getFecha());
        dto.setEstado(pedido.getEstado());
        dto.setTotal(pedido.getTotal());

        if (pedido.getDetalles() != null) {
            List<PedidoDetalleDTO> detalles = pedido.getDetalles().stream().map(det -> {
                PedidoDetalleDTO d = new PedidoDetalleDTO();
                d.setProductoId(det.getProducto().getId());
                d.setNombreProducto(det.getProducto().getNombre());
                d.setPrecioUnitario(det.getProducto().getPrecio());
                d.setCantidad(det.getCantidad());
                d.setSubtotal(det.getSubtotal());
                return d;
            }).collect(Collectors.toList());
            dto.setDetalles(detalles);
        }
        return dto;
    }

    private EnvioResponse mapToResponse(Envio envio) {
        EnvioResponse r = new EnvioResponse();
        r.setId(envio.getId());
        r.setPedidoId(envio.getPedido().getId());
        r.setCodigoVerificacion(envio.getCodigoVerificacion());
        r.setFechaEnvio(envio.getFechaEnvio());
        r.setFechaEntrega(envio.getFechaEntrega());
        r.setEstado(envio.getEstado());
        r.setConfirmadoComprador(envio.getConfirmadoComprador());
        r.setConfirmadoVendedor(envio.getConfirmadoVendedor());
        return r;
    }

    private PagoResponse mapToResponse(Pago pago) {
        PagoResponse r = new PagoResponse();
        r.setId(pago.getId());
        r.setPedidoId(pago.getPedido().getId());
        r.setMontoTotal(pago.getMontoTotal());
        r.setComision(pago.getComision());   // ya viene calculada de la BD
        r.setCodigoVerificacion(pago.getCodigoVerificacion());
        r.setMetodoPago(pago.getMetodoPago());
        r.setFechaPago(pago.getFechaPago());
        r.setEstado(pago.getEstado());
        return r;
    }
}
