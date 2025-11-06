import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { CarritoService } from '../../carrito';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.scss']
})
export class Navbar implements OnInit {
  contadorCarrito = 0;
  rol: string | null = null;

  constructor(
    private carritoService: CarritoService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.carritoService.contador$.subscribe(cantidad => {
      this.contadorCarrito = cantidad;
    });

    // Obtener el rol actual
    this.rol = this.auth.obtenerRol();
  }

  logout() {
    this.auth.logout();
    this.rol = null;
    this.router.navigate(['/login']);
  }
}

