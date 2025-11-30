import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class CompradorGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(): boolean {
    const rol = this.auth.obtenerRol();
    if (rol === 'comprador') return true;

    this.router.navigate(['/login']);
    return false;
  }
}
