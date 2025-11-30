package com.marketplace.controller;

import com.marketplace.dto.VendedorDashboardDTO;
import com.marketplace.entity.Usuario;
import com.marketplace.repository.UsuarioRepository;
import com.marketplace.service.VendedorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/vendedor")
public class VendedorController {

    private final VendedorService vendedorService;
    private final UsuarioRepository usuarioRepository;

    public VendedorController(VendedorService vendedorService,
                              UsuarioRepository usuarioRepository) {
        this.vendedorService = vendedorService;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }
        String correo = auth.getName();
        return usuarioRepository.findByCorreoAndEstadoTrue(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
    }

    @GetMapping("/dashboard")
    public VendedorDashboardDTO getDashboard() {
        Usuario u = getUsuarioAutenticado();
        return vendedorService.obtenerDashboard(u.getId());
    }
}
