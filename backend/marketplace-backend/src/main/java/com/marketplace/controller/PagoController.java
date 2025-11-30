package com.marketplace.controller;

import com.marketplace.dto.PagoResponse;
import com.marketplace.entity.Pago;
import com.marketplace.entity.Usuario;
import com.marketplace.repository.PagoRepository;
import com.marketplace.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoRepository pagoRepository;
    private final UsuarioRepository usuarioRepository;

    public PagoController(PagoRepository pagoRepository,
                          UsuarioRepository usuarioRepository) {
        this.pagoRepository = pagoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // --------- Helpers ---------
    private Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }
        String correo = auth.getName();
        return usuarioRepository.findByCorreoAndEstadoTrue(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
    }

    // --------- Listar pagos como COMPRADOR ---------
    @GetMapping("/comprador")
    public List<PagoResponse> listarPagosComoComprador() {
        Usuario u = getUsuarioAutenticado();
        return pagoRepository.findByComprador_Id(u.getId())
                .stream()
                .map(PagoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // --------- Listar pagos como VENDEDOR ---------
    @GetMapping("/vendedor")
    public List<PagoResponse> listarPagosComoVendedor() {
        Usuario u = getUsuarioAutenticado();
        return pagoRepository.findByVendedor_Id(u.getId())
                .stream()
                .map(PagoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // --------- Confirmación COMPRADOR (recibió el pedido) ---------
    @PutMapping("/{id}/confirmar-comprador")
    public ResponseEntity<Void> confirmarComoComprador(@PathVariable Integer id) {
        Usuario u = getUsuarioAutenticado();

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));

        if (!pago.getComprador().getId().equals(u.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes confirmar este pago");
        }

        pago.setConfirmadoComprador(true);
        pagoRepository.save(pago);
        return ResponseEntity.noContent().build();
    }

    // --------- Confirmación VENDEDOR (envió/entregó) ---------
    @PutMapping("/{id}/confirmar-vendedor")
    public ResponseEntity<Void> confirmarComoVendedor(@PathVariable Integer id) {
        Usuario u = getUsuarioAutenticado();

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));

        if (!pago.getVendedor().getId().equals(u.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes confirmar este pago");
        }

        pago.setConfirmadoVendedor(true);
        pagoRepository.save(pago);
        return ResponseEntity.noContent().build();
    }
}
