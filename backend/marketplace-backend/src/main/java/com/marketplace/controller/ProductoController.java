package com.marketplace.controller;

import com.marketplace.dto.ProductoRequest;
import com.marketplace.dto.ProductoResponse;
import com.marketplace.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Listar productos con filtros opcionales: categoriaId, vendedorId, q (texto)
    @GetMapping
    public List<ProductoResponse> listar(
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Integer vendedorId,
            @RequestParam(required = false) String q
    ) {
        return productoService.listar(categoriaId, vendedorId, q);
    }

    // Obtener producto por ID
    @GetMapping("/{id}")
    public ProductoResponse obtenerPorId(@PathVariable Integer id) {
        return productoService.obtenerPorId(id);
    }

    // Crear producto
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponse crear(@RequestBody ProductoRequest request) {
        return productoService.crear(request);
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable Integer id, @RequestBody ProductoRequest request) {
        return productoService.actualizar(id, request);
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
    }
}
