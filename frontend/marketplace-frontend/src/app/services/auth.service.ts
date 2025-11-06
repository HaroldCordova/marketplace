import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private usuarioActual: { email: string; rol: string } | null = null;

  login(email: string, rol: string) {
    this.usuarioActual = { email, rol };
    localStorage.setItem('usuario', JSON.stringify(this.usuarioActual));
  }

  logout() {
    this.usuarioActual = null;
    localStorage.removeItem('usuario');
  }

  obtenerUsuario() {
    if (!this.usuarioActual) {
      const guardado = localStorage.getItem('usuario');
      if (guardado) this.usuarioActual = JSON.parse(guardado);
    }
    return this.usuarioActual;
  }

  obtenerRol(): string | null {
    return this.obtenerUsuario()?.rol ?? null;
  }

  estaAutenticado(): boolean {
    return !!this.obtenerUsuario();
  }
}
