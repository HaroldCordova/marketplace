package com.marketplace.service;

import com.marketplace.dto.ProductoRequest;
import com.marketplace.dto.ProductoResponse;
import com.marketplace.entity.Categoria;
import com.marketplace.entity.Producto;
import com.marketplace.entity.Tienda;
import com.marketplace.entity.Usuario;
import com.marketplace.repository.CategoriaRepository;
import com.marketplace.repository.ProductoRepository;
import com.marketplace.repository.TiendaRepository;
import com.marketplace.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TiendaRepository tiendaRepository;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository,
                           UsuarioRepository usuarioRepository,
                           TiendaRepository tiendaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tiendaRepository = tiendaRepository;
    }

    // Listar productos con filtros opcionales
    public List<ProductoResponse> listar(Integer categoriaId, Integer vendedorId, String q) {
        return productoRepository.buscarFiltrado(categoriaId, vendedorId, q)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Obtener un producto por ID
    public ProductoResponse obtenerPorId(Integer id) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        return toResponse(p);
    }

    // Crear producto
    public ProductoResponse crear(ProductoRequest request) {

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no válida"));

        Usuario vendedor = usuarioRepository.findById(request.getIdVendedor())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vendedor no válido"));

        Tienda tienda = null;
        if (request.getIdTienda() != null) {
            tienda = tiendaRepository.findById(request.getIdTienda())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tienda no válida"));
        }

        Producto p = new Producto();
        p.setNombre(request.getNombre());
        p.setDescripcion(request.getDescripcion());
        p.setPrecio(request.getPrecio());
        p.setStock(request.getStock() != null ? request.getStock() : 0);
        p.setImagen(request.getImagen());
        p.setCategoria(categoria);
        p.setVendedor(vendedor);
        p.setTienda(tienda);
        p.setFechaRegistro(LocalDateTime.now());
        p.setVistas(0);
        p.setVentas(0);

        productoRepository.save(p);
        return toResponse(p);
    }

    // Actualizar producto
    public ProductoResponse actualizar(Integer id, ProductoRequest request) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (request.getNombre() != null) p.setNombre(request.getNombre());
        if (request.getDescripcion() != null) p.setDescripcion(request.getDescripcion());
        if (request.getPrecio() != null) p.setPrecio(request.getPrecio());
        if (request.getStock() != null) p.setStock(request.getStock());
        if (request.getImagen() != null) p.setImagen(request.getImagen());

        if (request.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría no válida"));
            p.setCategoria(categoria);
        }

        if (request.getIdTienda() != null) {
            Tienda tienda = tiendaRepository.findById(request.getIdTienda())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tienda no válida"));
            p.setTienda(tienda);
        }

        productoRepository.save(p);
        return toResponse(p);
    }

    // Eliminar producto
    public void eliminar(Integer id) {
        Producto p = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        productoRepository.delete(p);
    }

    // Conversión a DTO
    private ProductoResponse toResponse(Producto p) {
        ProductoResponse dto = new ProductoResponse();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        dto.setImagen(p.getImagen());

        if (p.getCategoria() != null) {
            dto.setIdCategoria(p.getCategoria().getId());
            dto.setNombreCategoria(p.getCategoria().getNombre());
        }

        if (p.getVendedor() != null) dto.setIdVendedor(p.getVendedor().getId());
        if (p.getTienda() != null) dto.setIdTienda(p.getTienda().getId());

        return dto;
    }
}
