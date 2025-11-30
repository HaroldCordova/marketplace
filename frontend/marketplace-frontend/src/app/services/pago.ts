import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

// ==== ESTA INTERFAZ ES LA QUE NECESITAN mis-pedidos y dashboard ====
export interface PagoCliente {
  id: number;
  pedidoId: number;
  // opcionales por si el backend no los envía siempre
  compradorId?: number;
  vendedorId?: number;

  montoTotal: number;
  montoVendedor: number;
  comision?: number;

  codigoVerificacion?: string;
  metodoPago?: string;
  fechaPago?: string;

  estado: string;
  confirmadoComprador: boolean | null;
  confirmadoVendedor: boolean | null;
  liberadoAdmin: boolean | null;
}

@Injectable({ providedIn: 'root' })
export class PagoService {

   private apiUrl = `${environment.apiBaseUrl}/api/pagos`;

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

  // Pagos donde soy COMPRADOR
  getPagosComoComprador(): Observable<PagoCliente[]> {
    return this.http.get<PagoCliente[]>(
      `${this.apiUrl}/comprador`,
      this.buildOptions()
    );
  }

  // Pagos donde soy VENDEDOR
  getPagosComoVendedor(): Observable<PagoCliente[]> {
    return this.http.get<PagoCliente[]>(
      `${this.apiUrl}/vendedor`,
      this.buildOptions()
    );
  }

  // Solo pagos RETENIDOS del vendedor (para dashboard)
  getPagosVendedorRetenidos(): Observable<PagoCliente[]> {
    return this.getPagosComoVendedor().pipe(
      map(pagos => pagos.filter(p => p.estado === 'retenido'))
    );
  }

  // Confirmación comprador
  confirmarComprador(pagoId: number) {
    return this.http.put<void>(
      `${this.apiUrl}/${pagoId}/confirmar-comprador`,
      {},
      this.buildOptions()
    );
  }

  // Confirmación vendedor
  confirmarVendedor(pagoId: number) {
    return this.http.put<void>(
      `${this.apiUrl}/${pagoId}/confirmar-vendedor`,
      {},
      this.buildOptions()
    );
  }
}
