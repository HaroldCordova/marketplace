import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

export interface PasoFormalizacion {
  id: number;
  vendedorId: number;
  paso: string;
  completado: boolean;
  fechaActualizacion?: string;
}

@Injectable({ providedIn: 'root' })
export class FormalizacionService {

  private apiUrl = `${environment.apiBaseUrl}/api/formalizacion`;

  constructor(
    private http: HttpClient,
    private auth: AuthService
  ) {}

  private buildOptions() {
    const token =
      (this.auth as any).token ??
      (this.auth as any).getToken?.();
    return token
      ? { headers: new HttpHeaders().set('Authorization', 'Bearer ' + token) }
      : {};
  }

  // Lista de pasos del vendedor logueado
  getMisPasos(): Observable<PasoFormalizacion[]> {
    return this.http.get<PasoFormalizacion[]>(
      `${this.apiUrl}/mis-pasos`,
      this.buildOptions()
    );
  }

  // Actualizar un paso concreto
  actualizarPaso(pasoId: number, completado: boolean): Observable<PasoFormalizacion> {
    return this.http.put<PasoFormalizacion>(
      `${this.apiUrl}/mis-pasos/${pasoId}`,
      { completado },
      this.buildOptions()
    );
  }
}
