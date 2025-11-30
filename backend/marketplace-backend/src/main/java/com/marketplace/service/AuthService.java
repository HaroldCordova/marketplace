package com.marketplace.service;

import com.marketplace.dto.LoginRequest;
import com.marketplace.dto.LoginResponse;
import com.marketplace.dto.RegisterRequest;
import com.marketplace.entity.Usuario;
import com.marketplace.repository.UsuarioRepository;
import com.marketplace.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // LOGIN seguro usando PasswordEncoder.matches(...)
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository
                .findByCorreoAndEstadoTrue(request.getCorreo())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        // Compara hash BCrypt almacenado con la contraseña en claro recibida
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        String nombreCompleto = usuario.getNombre() + " " + usuario.getApellido();

        String token = jwtUtil.generateToken(
                usuario.getCorreo(),
                Map.of(
                        "id", usuario.getId(),
                        "rol", usuario.getRol(),
                        "nombre", nombreCompleto
                )
        );

        LoginResponse response = new LoginResponse();
        response.setId(usuario.getId());
        response.setNombreCompleto(nombreCompleto);
        response.setRol(usuario.getRol());
        response.setToken(token);

        return response;
    }

    // REGISTER seguro: guarda la password hasheada (BCrypt)
    public LoginResponse register(RegisterRequest request) {

        // 1) Validaciones simples
        if (request.getCorreo() == null || request.getCorreo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Correo obligatorio");
        }
        if (request.getPassword() == null || request.getPassword().length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña inválida (mínimo 4 caracteres)");
        }
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está registrado");
        }

        // 2) Crear entidad Usuario
        Usuario u = new Usuario();
        u.setNombre(request.getNombre());
        u.setApellido(request.getApellido());
        u.setCorreo(request.getCorreo());
        // Hash de la contraseña
        u.setPassword(passwordEncoder.encode(request.getPassword()));
        u.setTelefono(request.getTelefono());
        u.setDireccion(request.getDireccion());

        String rol = request.getRol();
        if (rol == null || rol.isBlank()) rol = "comprador";
        u.setRol(rol);

        u.setEstado(true);
        u.setFechaRegistro(LocalDateTime.now());

        usuarioRepository.save(u);

        // 3) Generar token con el rol
        String nombreCompleto = u.getNombre() + " " + u.getApellido();

        String token = jwtUtil.generateToken(
                u.getCorreo(),
                Map.of(
                        "id", u.getId(),
                        "rol", u.getRol(),
                        "nombre", nombreCompleto
                )
        );

        LoginResponse response = new LoginResponse();
        response.setId(u.getId());
        response.setNombreCompleto(nombreCompleto);
        response.setRol(u.getRol());
        response.setToken(token);

        return response;
    }
}
