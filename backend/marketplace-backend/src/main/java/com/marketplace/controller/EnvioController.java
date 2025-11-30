package com.marketplace.controller;

import com.marketplace.dto.ConfirmarEnvioRequest;
import com.marketplace.dto.CrearEnvioRequest;
import com.marketplace.dto.EnvioDTO;
import com.marketplace.service.EnvioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @PostMapping
    public ResponseEntity<EnvioDTO> crear(@RequestBody CrearEnvioRequest request) {
        return ResponseEntity.ok(envioService.crearEnvio(request));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<EnvioDTO> confirmar(
            @PathVariable Integer id,
            @RequestBody ConfirmarEnvioRequest request
    ) {
        return ResponseEntity.ok(envioService.confirmarEnvio(id, request));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<EnvioDTO>> listarPorPedido(@PathVariable Integer pedidoId) {
        return ResponseEntity.ok(envioService.listarPorPedido(pedidoId));
    }
}