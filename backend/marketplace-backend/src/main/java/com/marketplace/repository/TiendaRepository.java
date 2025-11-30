package com.marketplace.repository;

import com.marketplace.entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TiendaRepository extends JpaRepository<Tienda, Integer> {

    Optional<Tienda> findFirstByIdVendedor(Integer idVendedor);

    List<Tienda> findByFormalizada(Boolean formalizada);
}
