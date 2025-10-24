import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';

@Component({
  selector: 'app-producto-detalle',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './producto-detalle.html',
  styleUrls: ['./producto-detalle.scss']
})
export class ProductoDetalle {
producto: any;

  productos = [
    {
      id: 1,
      nombre: 'Bolso artesanal de cuero',
      precio: 150,
      descripcion: 'Bolso hecho a mano con cuero natural, elaborado por artesanos locales. Ideal para uso diario.',
      imagen: 'http://3.bp.blogspot.com/-Dy_c5oZHuLU/TdaQKo7s5kI/AAAAAAAAApA/bsFnqiSsBOs/s1600/Doc+suela-azulino.jpg',
      categoria: 'Moda y Accesorios'
    },
    {
      id: 2,
      nombre: 'Taza de cerámica pintada a mano',
      precio: 40,
      descripcion: 'Taza artesanal con detalles únicos pintados a mano por artistas peruanos.',
      imagen: 'https://i.etsystatic.com/24345586/r/il/f4c017/2484000351/il_1588xN.2484000351_lr2x.jpg',
      categoria: 'Artesanías'
    },
    {
      id: 3,
      nombre: 'Pulsera tejida artesanal',
      precio: 25,
      descripcion: 'Pulsera colorida hecha con hilo peruano resistente. Perfecta para regalo o uso personal.',
      imagen: 'https://i.pinimg.com/originals/c6/be/f8/c6bef8f3bbd6e3e8ad40625d903349c1.jpg',
      categoria: 'Moda y Accesorios'
    },
    {
      id: 4,
      nombre: 'Lámpara decorativa de bambú',
      precio: 90,
      descripcion: 'Lámpara ecológica fabricada con bambú. Aporta calidez y estilo a cualquier ambiente.',
      imagen: 'https://tse2.mm.bing.net/th/id/OIP.oK8DRr5ujp_EdGPrP4qeigHaHa?pid=Api&P=0&h=180',
      categoria: 'Hogar y Cocina'
    },
    {
      id: 5,
      nombre: 'Shampoo natural de romero',
      precio: 35,
      imagen: 'https://media.sube.la/media/12914/products/vowdsbxNmw.jpg',
      categoria: 'Salud y Belleza'
    }
  ];

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.producto = this.productos.find(p => p.id === id);
  }

  agregarAlCarrito() {
    alert(`🛒 "${this.producto.nombre}" agregado al carrito.`);
  }
}
