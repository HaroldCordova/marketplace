import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface ResumenAdmin {
  usuariosActivos: number;
  categoriasActivas: number;
  reportesPendientes: number;
  pagosPendientes: number;
  montoRetenido: number;
  totalComisiones: number;
  comisionesMesActual: number;
  updatedAt: string;
}

export interface PagoAdmin {
  id: number;
  pedidoId: number;
  compradorId: number;
  vendedorId: number;
  montoTotal: number;
  montoVendedor: number;
  estado: string;
  fechaPago?: string;
  comision?: number;
  codigoVerificacion?: string;
  confirmadoComprador?: boolean;
  confirmadoVendedor?: boolean;
  liberadoAdmin?: boolean;
}

export interface EnvioAdmin {
  id: number;
  pedidoId: number;
  codigoVerificacion: string;
  estado: string;
  fechaEnvio?: string | null;
  fechaEntrega?: string | null;
  confirmadoComprador: boolean;
  confirmadoVendedor: boolean;
}

@Injectable({ providedIn: 'root' })
export class AdminService {

private apiUrl = `${environment.apiBaseUrl}/api/admin`;

  private resumenSubj = new BehaviorSubject<ResumenAdmin | null>(null);
  resumen$ = this.resumenSubj.asObservable();

  constructor(private http: HttpClient) {}

  // --------- RESUMEN ---------
  fetchResumen(): Observable<ResumenAdmin> {
    return this.http.get<ResumenAdmin>(`${this.apiUrl}/resumen`).pipe(
      tap(r => this.resumenSubj.next(r)),
      catchError(err => {
        console.error('Error obteniendo resumen admin', err);

        const fallback: ResumenAdmin = {
          usuariosActivos: 0,
          categoriasActivas: 0,
          reportesPendientes: 0,
          pagosPendientes: 0,
          montoRetenido: 0,
          totalComisiones: 0,
          comisionesMesActual: 0,
          updatedAt: new Date().toISOString()
        };

        if (!this.resumenSubj.value) {
          this.resumenSubj.next(fallback);
        }
        return of(fallback);
      })
    );
  }

  getResumen() {
    return this.resumen$;
  }

  // --------- PAGOS ---------
  getPagosPendientes(): Observable<PagoAdmin[]> {
    return this.http.get<PagoAdmin[]>(`${this.apiUrl}/pagos/retenidos`);
  }

  liberarPago(pagoId: number) {
    return this.http.put<void>(`${this.apiUrl}/pagos/${pagoId}/liberar`, {});
  }

  // --------- ENVIOS ---------
  getEnviosPendientes(): Observable<EnvioAdmin[]> {
    return this.http.get<EnvioAdmin[]>(`${this.apiUrl}/envios/pendientes`);
  }
}
