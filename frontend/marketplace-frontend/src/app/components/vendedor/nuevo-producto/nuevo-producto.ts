import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ProductoService, CrearProductoRequest } from '../../../services/producto';
import { CategoriaService, Categoria } from '../../../services/categoria';
import { AuthService } from '../../../services/auth.service';
import { ToastService } from '../../shared/toast.service';

@Component({
  selector: 'app-nuevo-producto',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './nuevo-producto.html',
  styleUrls: ['./nuevo-producto.scss']
})
export class NuevoProducto implements OnInit {

  nombre = '';
  descripcion = '';
  precio: number | null = null;
  stock: number | null = null;
  imagenUrl = '';
  categoriaId: number | null = null;

  categorias: Categoria[] = [];

  constructor(
    private productoService: ProductoService,
    private categoriaService: CategoriaService,
    private auth: AuthService,
    private router: Router,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.categoriaService.getCategorias().subscribe({
      next: (cats) => this.categorias = cats,
      error: (err) => console.error(err)
    });
  }

  guardarProducto() {
    if (!this.nombre || !this.precio || !this.stock || !this.categoriaId) {
      this.toast.warn('Completa los campos obligatorios.');
      return;
    }

    const vendedorId = this.auth.userId;
    if (!vendedorId) {
      this.toast.error('Tu sesión ha expirado. Vuelve a iniciar sesión.');
      this.router.navigate(['/login']);
      return;
    }

    const tiendaIdRaw = localStorage.getItem('tiendaId');
    const tiendaId = tiendaIdRaw ? Number(tiendaIdRaw) : null;

    if (!tiendaId) {
      this.toast.warn('Primero configura tu tienda en "Formalízate y Mi tienda".');
      this.router.navigate(['/vendedor/formalizate']);
      return;
    }

    const body: CrearProductoRequest = {
      nombre: this.nombre,
      descripcion: this.descripcion,
      precio: this.precio,
      stock: this.stock,
      imagen: this.imagenUrl,
      idCategoria: this.categoriaId,
      idVendedor: vendedorId,
      idTienda: tiendaId
    };

    this.productoService.crearProducto(body).subscribe({
      next: () => {
        this.toast.success('Producto creado correctamente.');
        this.router.navigate(['/vendedor/productos']);
      },
      error: (err) => {
        console.error(err);
        this.toast.error('No se pudo crear el producto.');
      }
    });
  }
}
