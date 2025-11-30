package com.marketplace.controller;

import com.marketplace.dto.TiendaRequest;
import com.marketplace.dto.TiendaResponse;
import com.marketplace.service.TiendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tiendas")
public class TiendaController {

    private final TiendaService tiendaService;

    public TiendaController(TiendaService tiendaService) {
        this.tiendaService = tiendaService;
    }

    // Vendedor crea su tienda
    @PostMapping
    public ResponseEntity<TiendaResponse> crear(@RequestBody TiendaRequest request) {
        return ResponseEntity.ok(tiendaService.crear(request));
    }

    // Vendedor consulta su propia tienda
    @GetMapping("/vendedor/{idVendedor}")
    public ResponseEntity<TiendaResponse> obtenerPorVendedor(@PathVariable Integer idVendedor) {
        return ResponseEntity.ok(tiendaService.obtenerPorVendedor(idVendedor));
    }

    // Actualizar tienda
    @PutMapping("/{id}")
    public ResponseEntity<TiendaResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody TiendaRequest request
    ) {
        return ResponseEntity.ok(tiendaService.actualizar(id, request));
    }

    // Admin: listar tiendas según formalización
    @GetMapping("/formalizadas/{flag}")
    public ResponseEntity<List<TiendaResponse>> listarPorFormalizacion(@PathVariable Boolean flag) {
        return ResponseEntity.ok(tiendaService.listarPorFormalizacion(flag));
    }
}
