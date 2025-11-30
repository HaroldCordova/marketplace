// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface LoginResponse {
  id: number;
  nombreCompleto: string;
  rol: 'admin' | 'vendedor' | 'comprador';
  token: string;
}

export interface RegisterRequest {
  nombre: string;
  apellido: string;
  correo: string;
  password: string;
  telefono?: string | null;
  direccion?: string | null;
  rol: 'comprador' | 'vendedor';
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // ✅ URL base apuntando al backend en Azure
  private apiUrl = `${environment.apiBaseUrl}/api/auth`;

  constructor(private http: HttpClient) {}

  // 🔹 LOGIN
  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, {
      correo: email,
      password: password
    });
  }

  // 🔹 REGISTRO
  register(data: RegisterRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/register`, data);
  }

  // 🔹 Guardar sesión
  saveSession(data: LoginResponse) {
    localStorage.setItem('token', data.token);
    localStorage.setItem('rol', data.rol);
    localStorage.setItem('nombre', data.nombreCompleto);
    localStorage.setItem('userId', data.id.toString());
  }

  logout() {
    localStorage.clear();
  }

  // 🔹 Getters
  get token() { return localStorage.getItem('token'); }
  get rol() { return localStorage.getItem('rol'); }
  get userId() { return Number(localStorage.getItem('userId')); }
  get nombre() { return localStorage.getItem('nombre'); }

  isLoggedIn(): boolean {
    return !!this.token;
  }

  obtenerRol(): string | null {
    return this.rol;
  }

  estaAutenticado(): boolean {
    return this.isLoggedIn();
  }
}
