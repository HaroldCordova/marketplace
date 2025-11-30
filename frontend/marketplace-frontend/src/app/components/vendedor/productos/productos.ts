import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProductoService, Producto } from '../../../services/producto';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-productos-vendedor',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './productos.html',
  styleUrls: ['./productos.scss']
})
export class ProductosVendedor implements OnInit {

  productos: Producto[] = [];
  cargando = true;

  constructor(
    private productoService: ProductoService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const vendedorId = this.auth.userId;
    if (!vendedorId) {
      // Si no hay sesión, regresamos al login
      this.router.navigate(['/login']);
      return;
    }

    this.productoService.getProductosVendedor(vendedorId).subscribe({
      next: (data) => {
        this.productos = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error(err);
        this.cargando = false;
      }
    });
  }

  nuevoProducto() {
    this.router.navigate(['/vendedor/nuevo']);
  }

  editarProducto(p: Producto) {
    // Aquí más adelante puedes navegar a pantalla de edición
    // this.router.navigate(['/vendedor/editar', p.id]);
  }

  eliminarProducto(p: Producto) {
    if (!confirm(`¿Eliminar el producto "${p.nombre}"?`)) return;

    this.productoService.eliminarProducto(p.id).subscribe({
      next: () => {
        this.productos = this.productos.filter(x => x.id !== p.id);
      },
      error: (err) => console.error(err)
    });
  }
}
