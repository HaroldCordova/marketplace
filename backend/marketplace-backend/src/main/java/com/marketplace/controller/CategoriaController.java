package com.marketplace.controller;

import com.marketplace.dto.CategoriaDto;
import com.marketplace.service.CategoriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<CategoriaDto> listar() {
        return categoriaService.listarTodas();
    }
}
