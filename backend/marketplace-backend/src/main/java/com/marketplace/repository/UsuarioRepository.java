package com.marketplace.repository;

import com.marketplace.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreoAndEstadoTrue(String correo);

    Optional<Usuario> findByCorreo(String correo); // <- añadido

    boolean existsByCorreo(String correo);

    long countByEstadoTrue();
}
