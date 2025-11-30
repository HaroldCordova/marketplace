import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

// Lo mínimo que necesitamos para mostrar pedidos.
// Si tu DTO tiene más campos, luego los vas agregando.
export interface PedidoDetalle {
  id: number;
  productoId: number;
  nombreProducto: string;
  cantidad: number;
  subtotal: number;
  imagenProducto?: string | null;
}

export interface Pedido {
  id: number;
  fecha: string;     // viene como string ISO del backend
  estado: string;
  total: number;
  detalles?: PedidoDetalle[];
}

export interface CrearPedidoItem {
  productoId: number;
  cantidad: number;
}

export interface CrearPedidoRequest {
  usuarioId: number;          // Id del comprador (Usuario)
  items: CrearPedidoItem[];   // productos que está comprando
}

@Injectable({
  providedIn: 'root'
})
export class PedidoService {

   private apiUrl = `${environment.apiBaseUrl}/api/pedidos`;

  constructor(private http: HttpClient) {}

  // Crear pedido (checkout desde el carrito)
  crearPedido(body: CrearPedidoRequest): Observable<Pedido> {
    return this.http.post<Pedido>(this.apiUrl, body);
  }

  // Pedidos del comprador logueado
  getPedidosUsuario(usuarioId: number): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.apiUrl}/usuario/${usuarioId}`);
  }

  // Pedidos relacionados a los productos de un vendedor (para futuro)
  getPedidosVendedor(vendedorId: number): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.apiUrl}/vendedor/${vendedorId}`);
  }

  // Obtener un pedido específico (detalle)
  getPedido(id: number): Observable<Pedido> {
    return this.http.get<Pedido>(`${this.apiUrl}/${id}`);
  }
}
