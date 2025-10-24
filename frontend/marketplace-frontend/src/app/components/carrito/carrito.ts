import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './carrito.html',
  styleUrls: ['./carrito.scss']
})
export class Carrito {
 carrito = [
    { nombre: 'Collar artesanal', precio: 45, cantidad: 1 },
    { nombre: 'Bolso ecológico', precio: 80, cantidad: 2 }
  ];

  total: number = this.carrito.reduce((acc, p) => acc + p.precio * p.cantidad, 0);

  actualizarTotal() {
    this.total = this.carrito.reduce((acc, p) => acc + p.precio * p.cantidad, 0);
  }

  eliminarDelCarrito(index: number) {
    this.carrito.splice(index, 1);
    this.actualizarTotal();
  }
}
