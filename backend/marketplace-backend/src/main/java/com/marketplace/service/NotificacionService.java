package com.marketplace.service;

import com.marketplace.dto.CrearNotificacionRequest;
import com.marketplace.dto.NotificacionDTO;
import com.marketplace.entity.Notificacion;
import com.marketplace.entity.Usuario;
import com.marketplace.repository.NotificacionRepository;
import com.marketplace.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public NotificacionService(NotificacionRepository notificacionRepository,
                               UsuarioRepository usuarioRepository) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<NotificacionDTO> listarPorUsuario(Integer usuarioId) {
        return notificacionRepository.findByUsuario_IdOrderByFechaDesc(usuarioId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public NotificacionDTO crear(CrearNotificacionRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));

        Notificacion n = new Notificacion();
        n.setUsuario(usuario);
        n.setTitulo(request.getTitulo());
        n.setMensaje(request.getMensaje());
        n.setLeido(false);
        n.setFecha(LocalDateTime.now());

        n = notificacionRepository.save(n);
        return mapToDTO(n);
    }

    public void marcarLeido(Integer notificacionId) {
        Notificacion n = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificación no encontrada."));
        n.setLeido(true);
        notificacionRepository.save(n);
    }

    private NotificacionDTO mapToDTO(Notificacion n) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(n.getId());
        dto.setUsuarioId(n.getUsuario().getId());
        dto.setTitulo(n.getTitulo());
        dto.setMensaje(n.getMensaje());
        dto.setLeido(n.getLeido());
        dto.setFecha(n.getFecha());
        return dto;
    }
}