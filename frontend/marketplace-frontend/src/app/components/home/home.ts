// src/app/components/home/home.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { ProductoService, Producto } from '../../services/producto';
import { CategoriaService, Categoria } from '../../services/categoria';
import { CarritoService } from '../../services/carrito';
import { ToastService } from '../shared/toast.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, ],
  templateUrl: './home.html',
  styleUrls: ['./home.scss']
})
export class Home implements OnInit {

  categorias: Categoria[] = [];
  productosDestacados: Producto[] = [];

  diferenciales = [
    {
      icono: 'bi bi-truck',
      titulo: 'Envíos a nivel nacional',
      descripcion: 'Tus productos pueden llegar a clientes de todo el país.'
    },
    {
      icono: 'bi bi-shield-check',
      titulo: 'Pagos protegidos',
      descripcion: 'El pago se libera solo cuando se confirma la entrega.'
    },
    {
      icono: 'bi bi-bag-heart',
      titulo: 'Apoyo al emprendedor',
      descripcion: 'Diseñado para pequeños negocios y productores locales.'
    },
    {
      icono: 'bi bi-bar-chart-line',
      titulo: 'Comisiones claras',
      descripcion: 'Modelo de comisiones transparente y sin letras pequeñas.'
    }
  ];

  constructor(
    private productoService: ProductoService,
    private categoriaService: CategoriaService,
    private carrito: CarritoService,
    private toast: ToastService,
    public router: Router          // 👈 AHORA ES PUBLIC
  ) {}

  ngOnInit(): void {
  this.categoriaService.getCategorias().subscribe({
    next: (c) => (this.categorias = c)
  });

  this.productoService.getProductos().subscribe({
    next: (prods) => {
      console.log('Productos desde API:', prods);  // 👈 agrega esto
      this.productosDestacados = prods.slice(0, 8);
    },
    error: (err) => {
      console.error('Error cargando productos', err);
    }
  });
}

  filtrarPorCategoria(cat: Categoria) {
    this.router.navigate(['/marketplace'], { queryParams: { categoriaId: cat.id } });
  }

  agregarAlCarrito(p: Producto) {
    this.carrito.agregarProducto(
      p.id,
      p.nombre,
      p.precio,
      p.imagen
    );
    this.toast.success('Producto añadido al carrito');
  }

  verDetalle(p: Producto) {
    this.router.navigate(['/producto', p.id]);
  }
}
