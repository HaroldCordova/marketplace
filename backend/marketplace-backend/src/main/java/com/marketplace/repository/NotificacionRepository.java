package com.marketplace.repository;

import com.marketplace.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByUsuario_IdOrderByFechaDesc(Integer usuarioId);

    long countByUsuario_IdAndLeidoFalse(Integer usuarioId);
}
