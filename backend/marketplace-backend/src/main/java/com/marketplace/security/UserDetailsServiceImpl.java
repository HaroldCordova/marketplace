package com.marketplace.security;

import com.marketplace.entity.Usuario;
import com.marketplace.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByCorreoAndEstadoTrue(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Convertir el rol (ej: "vendedor") a GrantedAuthority
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(u.getRol()));

        return new org.springframework.security.core.userdetails.User(
                u.getCorreo(),
                u.getPassword(), // debe ser hash BCrypt
                u.getEstado() != null && u.getEstado(), // enabled
                true, true, true,
                authorities
        );
    }
}
