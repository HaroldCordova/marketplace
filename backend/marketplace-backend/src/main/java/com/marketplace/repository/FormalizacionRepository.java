package com.marketplace.repository;

import com.marketplace.entity.Formalizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FormalizacionRepository extends JpaRepository<Formalizacion, Integer> {

    // Todos los pasos de un vendedor
    List<Formalizacion> findByVendedor_Id(Integer vendedorId);

    // Un paso concreto de un vendedor
    Optional<Formalizacion> findByVendedor_IdAndPaso(Integer vendedorId, String paso);
}
