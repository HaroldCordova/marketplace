package com.marketplace.service;

import com.marketplace.dto.ActualizarFormalizacionRequest;
import com.marketplace.dto.FormalizacionPasoDTO;
import com.marketplace.entity.Formalizacion;
import com.marketplace.entity.Notificacion;
import com.marketplace.entity.Usuario;
import com.marketplace.repository.FormalizacionRepository;
import com.marketplace.repository.NotificacionRepository;
import com.marketplace.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormalizacionService {

    private final FormalizacionRepository formalizacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionRepository notificacionRepository;

    public FormalizacionService(FormalizacionRepository formalizacionRepository,
                                UsuarioRepository usuarioRepository,
                                NotificacionRepository notificacionRepository) {
        this.formalizacionRepository = formalizacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionRepository = notificacionRepository;
    }

    // ================== LISTAR PASOS ==================

    /** Lista los pasos de formalización de un vendedor.
     *  Si no tiene pasos, puedes crear los 4 por defecto aquí si quieres. */
    public List<FormalizacionPasoDTO> listarPasos(Integer vendedorId) {
        List<Formalizacion> pasos = formalizacionRepository.findByVendedor_Id(vendedorId);

        // OPCIONAL: crear pasos por defecto si no tiene ninguno aún
        if (pasos.isEmpty()) {
            Usuario vendedor = usuarioRepository.findById(vendedorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendedor no encontrado."));

            String[] nombres = {
                    "Crear cuenta en la SUNAT",
                    "Obtener RUC",
                    "Registrar tu marca",
                    "Emitir boletas electrónicas"
            };

            for (String nombre : nombres) {
                Formalizacion f = new Formalizacion();
                f.setVendedor(vendedor);
                f.setPaso(nombre);
                f.setCompletado(false);
                f.setFechaActualizacion(LocalDateTime.now());
                formalizacionRepository.save(f);
            }

            pasos = formalizacionRepository.findByVendedor_Id(vendedorId);
        }

        return pasos.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ================== CREAR PASO (poco usado, pero lo dejamos) ==================

    public FormalizacionPasoDTO crearPaso(Integer vendedorId, String paso) {
        Usuario vendedor = usuarioRepository.findById(vendedorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendedor no encontrado."));

        Formalizacion f = new Formalizacion();
        f.setVendedor(vendedor);
        f.setPaso(paso);
        f.setCompletado(false);
        f.setFechaActualizacion(LocalDateTime.now());

        f = formalizacionRepository.save(f);
        return mapToDTO(f);
    }

    // ================== ACTUALIZAR PASO POR ID ==================

    public FormalizacionPasoDTO actualizarPaso(Integer id, ActualizarFormalizacionRequest request) {
        Formalizacion f = formalizacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paso de formalización no encontrado."
                ));

        if (request.getCompletado() != null &&
            !request.getCompletado().equals(f.getCompletado())) {

            boolean nuevoEstado = request.getCompletado();
            f.setCompletado(nuevoEstado);
            f.setFechaActualizacion(LocalDateTime.now());
            f = formalizacionRepository.save(f);

            // Crear notificación para el vendedor
            Notificacion n = new Notificacion();
            n.setUsuario(f.getVendedor());
            n.setTitulo("Actualización de formalización");
            n.setMensaje(
                    (nuevoEstado ? "Marcaste como completado" : "Marcaste como pendiente")
                            + " el paso: " + f.getPaso()
            );
            n.setLeido(false);
            n.setFecha(LocalDateTime.now());
            notificacionRepository.save(n);
        }

        return mapToDTO(f);
    }

    // ================== MAPPER ==================

    private FormalizacionPasoDTO mapToDTO(Formalizacion f) {
        FormalizacionPasoDTO dto = new FormalizacionPasoDTO();
        dto.setId(f.getId());
        dto.setVendedorId(f.getVendedor().getId());
        dto.setPaso(f.getPaso());
        dto.setCompletado(f.getCompletado());
        dto.setFechaActualizacion(f.getFechaActualizacion());
        return dto;
    }
}
