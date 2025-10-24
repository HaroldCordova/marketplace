import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CarritoService } from '../../carrito';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.scss']
})
export class Navbar implements OnInit {
   contadorCarrito = 0;

  constructor(private carritoService: CarritoService) {}

  ngOnInit() {
    this.carritoService.contador$.subscribe(cantidad => {
      this.contadorCarrito = cantidad;
    });
  }
}
