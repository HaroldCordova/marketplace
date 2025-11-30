package com.marketplace.service;

import com.marketplace.dto.CategoriaDto;
import com.marketplace.entity.Categoria;
import com.marketplace.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaDto> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private CategoriaDto toDto(Categoria categoria) {
        CategoriaDto dto = new CategoriaDto();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }
}
