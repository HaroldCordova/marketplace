import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Envio {
  id: number;
  pedidoId: number;
  codigoVerificacion: string;
  fechaEnvio: string;
  fechaEntrega?: string | null;
  estado: string;
  confirmadoComprador: boolean;
  confirmadoVendedor: boolean;
}

export interface ConfirmarEnvioRequest {
  pedidoId: number;
  rol: 'comprador' | 'vendedor';
}

@Injectable({
  providedIn: 'root'
})
export class EnvioService {

   private apiUrl = `${environment.apiBaseUrl}/api/envios`;

  constructor(private http: HttpClient) {}

  // Obtener envío por pedido
  getEnvioPorPedido(pedidoId: number): Observable<Envio> {
    return this.http.get<Envio>(`${this.apiUrl}/pedido/${pedidoId}`);
  }

  // Confirmar recepción/envío (según rol)
  confirmarEnvio(body: ConfirmarEnvioRequest): Observable<Envio> {
    return this.http.post<Envio>(`${this.apiUrl}/confirmar`, body);
  }
}
