import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Tienda {
  id: number;
  idVendedor: number;
  nombre: string;
  descripcion: string | null;
  ruc: string | null;
  formalizada: boolean;
  necesitaFormalizar: boolean;
  fechaRegistro: string;
}

export interface TiendaRequest {
  idVendedor: number;
  nombre: string;
  descripcion: string;
  ruc?: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class TiendaService {

  private apiUrl = `${environment.apiBaseUrl}/api/tiendas`;

  constructor(private http: HttpClient) {}

  crearTienda(body: TiendaRequest): Observable<Tienda> {
    return this.http.post<Tienda>(this.apiUrl, body);
  }

  obtenerPorVendedor(idVendedor: number): Observable<Tienda> {
    return this.http.get<Tienda>(`${this.apiUrl}/vendedor/${idVendedor}`);
  }

  actualizarTienda(id: number, body: TiendaRequest): Observable<Tienda> {
    return this.http.put<Tienda>(`${this.apiUrl}/${id}`, body);
  }

  listarPorFormalizacion(flag: boolean): Observable<Tienda[]> {
    return this.http.get<Tienda[]>(`${this.apiUrl}/formalizadas/${flag}`);
  }
}