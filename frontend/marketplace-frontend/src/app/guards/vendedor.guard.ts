import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class VendedorGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(): boolean {
    const rol = this.auth.obtenerRol();
    if (rol === 'vendedor') return true;

    this.router.navigate(['/login']);
    return false;
  }
}
