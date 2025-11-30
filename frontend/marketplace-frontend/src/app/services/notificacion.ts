// src/app/services/notificacion.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Notificacion {
  id: number;
  titulo: string;
  mensaje: string;
  leido: boolean;
  fecha: string;
}

export interface CrearNotificacionRequest {
  usuarioId: number;
  titulo: string;
  mensaje: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificacionService {

  private apiUrl = `${environment.apiBaseUrl}/api/notificaciones`;

  constructor(private http: HttpClient) {}

  getNotificacionesUsuario(usuarioId: number): Observable<Notificacion[]> {
    return this.http.get<Notificacion[]>(`${this.apiUrl}/usuario/${usuarioId}`);
  }

  marcarComoLeida(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/leer`, {});
  }

  crearNotificacion(body: CrearNotificacionRequest): Observable<Notificacion> {
    return this.http.post<Notificacion>(this.apiUrl, body);
  }
}
