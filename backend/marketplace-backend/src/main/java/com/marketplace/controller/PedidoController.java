package com.marketplace.controller;

import com.marketplace.dto.CrearPedidoRequest;
import com.marketplace.dto.PedidoResponse;
import com.marketplace.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@RequestBody CrearPedidoRequest request) {
        return ResponseEntity.ok(pedidoService.crearPedido(request));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponse>> listarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(id));
    }
}
