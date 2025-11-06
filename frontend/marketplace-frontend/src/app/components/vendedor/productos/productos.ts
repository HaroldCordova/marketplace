import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-productos-vendedor',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './productos.html',
  styleUrls: ['./productos.scss']
})
export class ProductosVendedor {
  productos = [
    {
      nombre: 'Bolso artesanal de cuero',
      precio: 150,
      stock: 10,
      categoria: 'Moda y Accesorios',
      imagen: 'http://3.bp.blogspot.com/-Dy_c5oZHuLU/TdaQKo7s5kI/AAAAAAAAApA/bsFnqiSsBOs/s1600/Doc+suela-azulino.jpg'
    },
    {
      nombre: 'Taza pintada a mano',
      precio: 40,
      stock: 25,
      categoria: 'Artesanías',
      imagen: 'https://i.etsystatic.com/24345586/r/il/f4c017/2484000351/il_1588xN.2484000351_lr2x.jpg'
    }
  ];

  editarProducto(producto: any) {
    alert(`Editar: ${producto.nombre}`);
  }

  eliminarProducto(producto: any) {
    if (confirm(`¿Deseas eliminar "${producto.nombre}"?`)) {
      this.productos = this.productos.filter(p => p !== producto);
    }
  }
}
