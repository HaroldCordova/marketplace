package com.marketplace.repository;

import com.marketplace.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query("""
       SELECT p FROM Producto p
       JOIN FETCH p.categoria
       JOIN FETCH p.vendedor
       LEFT JOIN FETCH p.tienda
       WHERE (:categoriaId IS NULL OR p.categoria.id = :categoriaId)
         AND (:vendedorId IS NULL OR p.vendedor.id = :vendedorId)
         AND (:q IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')))
       """)
List<Producto> buscarFiltrado(
        @Param("categoriaId") Integer categoriaId,
        @Param("vendedorId") Integer vendedorId,
        @Param("q") String q
);
    // Cuenta productos publicados por vendedor
    long countByVendedor_Id(@Param("vendedorId") Integer vendedorId);

}
