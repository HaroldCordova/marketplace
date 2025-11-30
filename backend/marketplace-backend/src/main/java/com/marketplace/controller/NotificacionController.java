package com.marketplace.controller;

import com.marketplace.dto.CrearNotificacionRequest;
import com.marketplace.dto.NotificacionDTO;
import com.marketplace.service.NotificacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionDTO>> listarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(notificacionService.listarPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<NotificacionDTO> crear(@RequestBody CrearNotificacionRequest request) {
        return ResponseEntity.ok(notificacionService.crear(request));
    }

    @PatchMapping("/{id}/leida")
    public ResponseEntity<Void> marcarLeida(@PathVariable Integer id) {
        notificacionService.marcarLeido(id);
        return ResponseEntity.noContent().build();
    }
}