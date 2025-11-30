import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Producto {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  stock: number;

  // Soportamos ambos nombres para la imagen
  imagen?: string | null;
  imagenUrl?: string | null;

  idCategoria: number;

  // Soportamos ambos nombres para la categoría
  nombreCategoria?: string;
  categoriaNombre?: string;

  idVendedor: number;
  idTienda: number;
}

export interface CrearProductoRequest {
  nombre: string;
  descripcion: string;
  precio: number;
  stock: number;
  imagen: string;
  idCategoria: number;
  idVendedor: number;
  idTienda: number;
}

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  private apiUrl = `${environment.apiBaseUrl}/api/productos`;

  constructor(private http: HttpClient) {}

  // 📌 Marketplace general con filtros opcionales (categoría y búsqueda)
  getProductos(categoriaId?: number, q?: string): Observable<Producto[]> {
    let params = new HttpParams();
    if (categoriaId) params = params.set('categoriaId', categoriaId);
    if (q) params = params.set('q', q);

    return this.http.get<Producto[]>(this.apiUrl, { params });
  }

  // 📌 Productos del vendedor actual
  getProductosVendedor(vendedorId: number): Observable<Producto[]> {
    const params = new HttpParams().set('vendedorId', vendedorId);
    return this.http.get<Producto[]>(this.apiUrl, { params });
  }

  // 📌 Detalle de producto
  getProducto(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/${id}`);
  }

  // 📌 Crear producto
  crearProducto(body: CrearProductoRequest): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, body);
  }

  // 📌 Actualizar producto
  actualizarProducto(id: number, body: Partial<CrearProductoRequest>): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}`, body);
  }

  // 📌 Eliminar producto
  eliminarProducto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // ============================
  // 🔁 ALIAS PARA CÓDIGO EXISTENTE
  // ============================

  /** Alias de getProductos() para no romper otros componentes */
  listar(): Observable<Producto[]> {
    return this.getProductos();
  }

  /** Alias de getProducto(id) para no romper otros componentes */
  obtenerPorId(id: number): Observable<Producto> {
    return this.getProducto(id);
  }
}
