package com.marketplace.controller;

import com.marketplace.dto.ActualizarFormalizacionRequest;
import com.marketplace.dto.FormalizacionPasoDTO;
import com.marketplace.entity.Usuario;
import com.marketplace.repository.UsuarioRepository;
import com.marketplace.service.FormalizacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/formalizacion")
public class FormalizacionController {

    private final FormalizacionService formalizacionService;
    private final UsuarioRepository usuarioRepository;

    public FormalizacionController(FormalizacionService formalizacionService,
                                   UsuarioRepository usuarioRepository) {
        this.formalizacionService = formalizacionService;
        this.usuarioRepository = usuarioRepository;
    }

    // ========= helper para saber quién es el vendedor logueado =========
    private Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        String correo = auth.getName();
        return usuarioRepository.findByCorreoAndEstadoTrue(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
    }

    // ========= ENDPOINTS ANTIGUOS (si los necesitas desde admin) =========

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<FormalizacionPasoDTO>> listarPorVendedor(@PathVariable Integer vendedorId) {
        return ResponseEntity.ok(formalizacionService.listarPasos(vendedorId));
    }

    @PostMapping("/vendedor/{vendedorId}")
    public ResponseEntity<FormalizacionPasoDTO> crearPaso(
            @PathVariable Integer vendedorId,
            @RequestParam String paso
    ) {
        return ResponseEntity.ok(formalizacionService.crearPaso(vendedorId, paso));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FormalizacionPasoDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody ActualizarFormalizacionRequest request
    ) {
        return ResponseEntity.ok(formalizacionService.actualizarPaso(id, request));
    }

    // ========= NUEVOS ENDPOINTS PARA EL VENDEDOR AUTENTICADO =========

    // Usado por Angular: GET /api/formalizacion/mis-pasos
    @GetMapping("/mis-pasos")
    public List<FormalizacionPasoDTO> listarMisPasos() {
        Usuario vendedor = getUsuarioAutenticado();
        return formalizacionService.listarPasos(vendedor.getId());
    }

    // Usado por Angular: PUT /api/formalizacion/mis-pasos/{id}
    @PutMapping("/mis-pasos/{id}")
    public FormalizacionPasoDTO actualizarMiPaso(
            @PathVariable Integer id,
            @RequestBody ActualizarFormalizacionRequest request
    ) {
        // reutilizamos la lógica que ya tienes en el service
        return formalizacionService.actualizarPaso(id, request);
    }
}
