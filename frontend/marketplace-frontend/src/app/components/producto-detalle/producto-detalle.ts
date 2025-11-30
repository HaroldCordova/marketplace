import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductoService, Producto } from '../../services/producto';

@Component({
  selector: 'app-producto-detalle',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './producto-detalle.html',
  styleUrls: ['./producto-detalle.scss']
})
export class ProductoDetalle implements OnInit {

  producto: Producto | null = null;
  similares: Producto[] = [];

  cargandoProducto = true;
  cargandoSimilares = false;

  cantidad = 1;

  constructor(
    private route: ActivatedRoute,
    private productoService: ProductoService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.cargandoProducto = false;
      return;
    }
    this.cargarProducto(id);
  }

  // ====== Cargar producto principal ======
  private cargarProducto(id: number): void {
    this.cargandoProducto = true;

    this.productoService.obtenerPorId(id).subscribe({
      next: (prod: Producto) => {
        this.producto = prod;
        this.cargandoProducto = false;
        this.cargarSimilares(prod);
      },
      error: (err: any) => {
        console.error('Error cargando producto', err);
        this.cargandoProducto = false;
      }
    });
  }

  // ====== Cargar productos similares (misma categoría) ======
  private cargarSimilares(prod: Producto): void {
    this.cargandoSimilares = true;

    this.productoService.listar().subscribe({
      next: (lista: Producto[]) => {
        this.similares = lista
          .filter(p => p.id !== prod.id && p.idCategoria === prod.idCategoria)
          .slice(0, 4);
        this.cargandoSimilares = false;
      },
      error: (err: any) => {
        console.error('Error cargando similares', err);
        this.cargandoSimilares = false;
      }
    });
  }

  // Imagen principal (soporta imagen o imagenUrl y usa un placeholder si viene null)
  get imagenPrincipal(): string {
    if (!this.producto) return 'assets/img/product-placeholder.png';
    return (this.producto.imagen ||
            this.producto.imagenUrl ||
            'assets/img/product-placeholder.png') as string;
  }

  // Nombre de categoría unificado
  get nombreCategoria(): string {
    if (!this.producto) return '';
    return this.producto.nombreCategoria ||
           this.producto.categoriaNombre ||
           '';
  }

  agregarAlCarrito(): void {
    if (!this.producto) return;
    // Aquí después conectarás tu servicio real de carrito/pagos
    alert(`🛒 "${this.producto.nombre}" (x${this.cantidad}) agregado al carrito.`);
  }
}
