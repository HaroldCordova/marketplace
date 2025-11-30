// src/app/components/navbar/navbar.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { CarritoService } from '../../services/carrito';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.scss']
})
export class Navbar implements OnInit, OnDestroy {

  contadorCarrito = 0;
  rol: string | null = null;
  usuarioNombre: string | null = null;

  private destroy$ = new Subject<void>();

  constructor(
    private carritoService: CarritoService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {

    // 🔹 Contador del carrito
    if (this.carritoService?.contador$) {
      this.carritoService.contador$
        .pipe(takeUntil(this.destroy$))
        .subscribe(c => this.contadorCarrito = c ?? 0);
    }

    // 🔹 Rol guardado en localStorage desde AuthService
    this.rol = this.auth.obtenerRol();

    // 🔹 Nombre del usuario logueado
    this.usuarioNombre = this.auth.nombre;
  }

  // 🔹 Getters útiles para la vista
  get isComprador() { return this.rol === 'comprador'; }
  get isVendedor() { return this.rol === 'vendedor'; }
  get isAdmin() { return this.rol === 'admin'; }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
