import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import { CarritoService, ItemCarrito } from '../../services/carrito';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../shared/toast.service';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './carrito.html',
  styleUrls: ['./carrito.scss']
})
export class Carrito implements OnInit {

  items: ItemCarrito[] = [];
  total = 0;

  constructor(
    private carritoService: CarritoService,
    private auth: AuthService,
    private toast: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarCarrito();
  }

  private cargarCarrito(): void {
    this.items = this.carritoService.obtenerItems();
    this.total = this.carritoService.obtenerTotal();
  }

  cambiarCantidad(item: ItemCarrito, nuevaCantidad: number | string) {
    const cant = Number(nuevaCantidad);
    if (isNaN(cant) || cant <= 0) {
      return;
    }
    this.carritoService.actualizarCantidad(item.productoId, cant);
    this.cargarCarrito();
  }

  eliminar(item: ItemCarrito) {
    if (!confirm(`¿Quitar "${item.nombre}" del carrito?`)) {
      return;
    }
    this.carritoService.eliminarProducto(item.productoId);
    this.cargarCarrito();
    this.toast.info('Producto eliminado del carrito');
  }

  seguirComprando() {
    this.router.navigate(['/marketplace']);
  }

  confirmarCompra() {
    if (this.items.length === 0) {
      this.toast.warning('Tu carrito está vacío.');
      return;
    }

    const rol = this.auth.obtenerRol();   // null si no ha iniciado sesión

    // 👉 Si no está logeado: lo enviamos a login
    if (!rol) {
      this.toast.info('Inicia sesión para confirmar tu compra.');
      this.router.navigate(['/login'], {
        queryParams: { redirectTo: '/carrito' }
      });
      return;
    }

    // 👉 Si está logeado pero no es comprador
    if (rol !== 'comprador') {
      this.toast.warning('Debes ingresar como comprador para finalizar la compra.');
      return;
    }

    // 👉 Aquí luego llamarás a tu API para crear Pedido + Pago + Envío
    this.toast.success('Compra registrada. Podrás ver el detalle en "Mis pedidos".');

    this.carritoService.vaciar();
    this.cargarCarrito();

    // Opcional: redirigir a mis pedidos
    this.router.navigate(['/mis-pedidos']);
  }
}
