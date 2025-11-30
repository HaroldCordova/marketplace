// src/app/components/marketplace/marketplace.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductoService, Producto } from '../../services/producto';
import { CarritoService } from '../../services/carrito';
import { ToastService } from '../shared/toast.service';

@Component({
  selector: 'app-marketplace',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './marketplace.html',
  styleUrls: ['./marketplace.scss']
})
export class Marketplace implements OnInit {

  // todos los productos desde el backend
  private productos: Producto[] = [];

  // productos que se muestran (con búsqueda / orden / categoría)
  productosFiltrados: Producto[] = [];

  textoBusqueda = '';
  ordenActual: 'relevancia' | 'precio-asc' | 'precio-desc' = 'relevancia';
  categoriaId?: number;

  cantidadCarrito = 0;

  constructor(
    private productoService: ProductoService,
    private carrito: CarritoService,
    private toast: ToastService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // leer categoria desde query params (viene desde el Home)
    this.route.queryParams.subscribe(params => {
      const cat = params['categoriaId'];
      this.categoriaId = cat ? Number(cat) : undefined;
      this.cargarProductos();
    });

    // si tu CarritoService tiene observable de contador, úsalo:
    if ((this.carrito as any).contador$) {
      (this.carrito as any).contador$.subscribe((n: number) => {
        this.cantidadCarrito = n;
      });
    } else {
      // versión simple: toma los datos desde localStorage
      this.cantidadCarrito = this.carrito.getCantidadTotal();
    }
  }

  cargarProductos() {
    this.productoService.getProductos().subscribe({
      next: (data) => {
        this.productos = data;
        this.aplicarFiltros();
      },
      error: (err) => {
        console.error(err);
        this.toast.error('No se pudieron cargar los productos.');
      }
    });
  }

  // 🔹 Aplica búsqueda, filtro por categoría y orden
  aplicarFiltros() {
    let lista = [...this.productos];

    // filtrar por categoría si viene en la URL
    if (this.categoriaId) {
      lista = lista.filter(p =>
        // por si el backend manda idCategoria o un objeto categoria
        (p as any).idCategoria === this.categoriaId ||
        ((p as any).categoria && (p as any).categoria.id === this.categoriaId)
      );
    }

    // búsqueda por texto
    const q = this.textoBusqueda.trim().toLowerCase();
    if (q) {
      lista = lista.filter(p =>
        p.nombre.toLowerCase().includes(q) ||
        (p.descripcion && p.descripcion.toLowerCase().includes(q))
      );
    }

    // orden
    switch (this.ordenActual) {
      case 'precio-asc':
        lista.sort((a, b) => a.precio - b.precio);
        break;
      case 'precio-desc':
        lista.sort((a, b) => b.precio - a.precio);
        break;
      default:
        // relevancia: por ahora lo dejamos tal cual viene
        break;
    }

    this.productosFiltrados = lista;
  }

  cambiarOrden(orden: 'relevancia' | 'precio-asc' | 'precio-desc') {
    this.ordenActual = orden;
    this.aplicarFiltros();
  }

  // 🛒 Añadir al carrito
  agregarAlCarrito(p: Producto) {
    this.carrito.agregarProducto(
      p.id,
      p.nombre,
      p.precio,
      p.imagen
    );
    this.cantidadCarrito = this.carrito.getCantidadTotal();
    this.toast.success('Producto añadido al carrito');
  }

  // 👀 Ver detalle
  verDetalle(p: Producto) {
    this.router.navigate(['/producto', p.id]);
  }

  // botón "Carrito" arriba
  irAlCarrito() {
    this.router.navigate(['/carrito']);
  }
}
