import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {CarritoService} from '../../carrito'

@Component({
  selector: 'app-marketplace',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './marketplace.html',
  styleUrls: ['./marketplace.scss']
})
export class Marketplace {
searchTerm: string = '';

  productos = [
    {
      id: 1,
      nombre: 'Cartera artesanal de cuero',
      precio: 120,
      imagen: 'http://3.bp.blogspot.com/-Dy_c5oZHuLU/TdaQKo7s5kI/AAAAAAAAApA/bsFnqiSsBOs/s1600/Doc+suela-azulino.jpg',
      descripcion: 'Hecha a mano por emprendedores locales con cuero natural.'
    },
    {
      id: 2,
      nombre: 'Café orgánico del norte',
      precio: 35,
      imagen: 'https://images.unsplash.com/photo-1511920170033-f8396924c348',
      descripcion: 'Café 100% natural de cooperativas peruanas certificadas.'
    },
    {
      id: 3,
      nombre: 'Pulsera tejida artesanal',
      precio: 25,
      imagen: 'https://i.pinimg.com/originals/c6/be/f8/c6bef8f3bbd6e3e8ad40625d903349c1.jpg',
      descripcion: 'Accesorio colorido elaborado por mujeres artesanas.'
    },
    {
      id: 4,
      nombre: 'Miel de abeja natural',
      precio: 40,
      imagen: 'https://tse4.mm.bing.net/th/id/OIP.54eqCU1HhacjRzyTDjYmCgHaE8?pid=Api&P=0&h=180',
      descripcion: 'Pura, sin conservantes ni aditivos.'
    }
  ];

   constructor(private carritoService: CarritoService) {}

  agregarAlCarrito(producto: any) {
    this.carritoService.agregarProducto(producto);
    alert(`🛒 "${producto.nombre}" agregado al carrito.`);
  }
}
