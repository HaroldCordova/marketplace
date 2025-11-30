package com.marketplace.service;

import com.marketplace.dto.TiendaRequest;
import com.marketplace.dto.TiendaResponse;
import com.marketplace.entity.Tienda;
import com.marketplace.repository.TiendaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TiendaService {

    private final TiendaRepository tiendaRepository;

    public TiendaService(TiendaRepository tiendaRepository) {
        this.tiendaRepository = tiendaRepository;
    }

    // Crear tienda (vendedor que recién se formaliza o abre tienda)
    public TiendaResponse crear(TiendaRequest request) {

        // Regla: un vendedor solo puede tener una tienda
        tiendaRepository.findFirstByIdVendedor(request.getIdVendedor())
                .ifPresent(t -> {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "El vendedor ya tiene una tienda registrada."
                    );
                });

        Tienda tienda = new Tienda();
        tienda.setIdVendedor(request.getIdVendedor());
        tienda.setNombre(request.getNombre());
        tienda.setDescripcion(request.getDescripcion());
        tienda.setRuc(request.getRuc());

        boolean formalizada = request.getRuc() != null && !request.getRuc().trim().isEmpty();
        tienda.setFormalizada(formalizada);

        Tienda guardada = tiendaRepository.save(tienda);
        return toResponse(guardada);
    }

    // Ver tienda del vendedor logueado
    public TiendaResponse obtenerPorVendedor(Integer idVendedor) {
        Tienda tienda = tiendaRepository.findFirstByIdVendedor(idVendedor)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El vendedor no tiene tienda registrada."));
        return toResponse(tienda);
    }

    // Actualizar datos de tienda (descripción, RUC, etc.)
    public TiendaResponse actualizar(Integer id, TiendaRequest request) {
        Tienda tienda = tiendaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tienda no encontrada."));

        tienda.setNombre(request.getNombre());
        tienda.setDescripcion(request.getDescripcion());
        tienda.setRuc(request.getRuc());
        boolean formalizada = request.getRuc() != null && !request.getRuc().trim().isEmpty();
        tienda.setFormalizada(formalizada);

        Tienda actualizada = tiendaRepository.save(tienda);
        return toResponse(actualizada);
    }

    // Vista para admin: listar tiendas formalizadas / no formalizadas
    public List<TiendaResponse> listarPorFormalizacion(Boolean formalizada) {
        return tiendaRepository.findByFormalizada(formalizada)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TiendaResponse toResponse(Tienda t) {
        TiendaResponse resp = new TiendaResponse();
        resp.setId(t.getId());
        resp.setIdVendedor(t.getIdVendedor());
        resp.setNombre(t.getNombre());
        resp.setDescripcion(t.getDescripcion());
        resp.setRuc(t.getRuc());
        resp.setFormalizada(t.getFormalizada());
        resp.setNecesitaFormalizar(t.getNecesitaFormalizar());
        resp.setFechaRegistro(t.getFechaRegistro());
        return resp;
    }
}
