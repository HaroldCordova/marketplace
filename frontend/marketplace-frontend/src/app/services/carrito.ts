// src/app/services/carrito.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface ItemCarrito {
  productoId: number;
  nombre: string;
  precioUnitario: number;
  cantidad: number;
  imagen?: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class CarritoService {

  private readonly STORAGE_KEY = 'carrito-marketplace';

  // 🔹 Estado interno del carrito
  private items: ItemCarrito[] = [];

  // 🔹 Subject para el contador del carrito (navbar, etc.)
  private contadorSubject = new BehaviorSubject<number>(0);
  contador$ = this.contadorSubject.asObservable();

  constructor() {
    this.cargarDeStorage();
  }

  // ==========================
  //  🔹 Persistencia
  // ==========================
  private cargarDeStorage(): void {
    try {
      const data = localStorage.getItem(this.STORAGE_KEY);
      if (data) {
        this.items = JSON.parse(data) as ItemCarrito[];
      }
    } catch (e) {
      console.error('Error leyendo carrito de storage', e);
      this.items = [];
    }
    this.actualizarContador();
  }

  private guardarEnStorage(): void {
    try {
      localStorage.setItem(this.STORAGE_KEY, JSON.stringify(this.items));
    } catch (e) {
      console.error('Error guardando carrito en storage', e);
    }
    this.actualizarContador();
  }

  private actualizarContador(): void {
    this.contadorSubject.next(this.getCantidadTotal());
  }

  // ==========================
  //  🔹 Getters
  // ==========================
  /** Cantidad total de ítems (para el ícono del carrito) */
  getCantidadTotal(): number {
    return this.items.reduce((acc, it) => acc + it.cantidad, 0);
  }

  /** Devuelve una copia de los ítems del carrito */
  obtenerItems(): ItemCarrito[] {
    return [...this.items];
  }

  /** Total en S/ del carrito */
  obtenerTotal(): number {
    return this.items.reduce(
      (acc, it) => acc + it.cantidad * it.precioUnitario,
      0
    );
  }

  // Alias por si en algún componente usamos getItems / getTotal
  getItems(): ItemCarrito[] {
    return this.obtenerItems();
  }

  getTotal(): number {
    return this.obtenerTotal();
  }

  // ==========================
  //  🔹 Operaciones principales
  // ==========================
  /** Agrega 1 unidad del producto (o aumenta la cantidad si ya existe) */
  agregarProducto(
    productoId: number,
    nombre: string,
    precioUnitario: number,
    imagen?: string | null
  ): void {
    const existente = this.items.find(i => i.productoId === productoId);

    if (existente) {
      existente.cantidad += 1;
    } else {
      this.items.push({
        productoId,
        nombre,
        precioUnitario,
        cantidad: 1,
        imagen
      });
    }

    this.guardarEnStorage();
  }

  /** Cambia la cantidad de un ítem */
  actualizarCantidad(productoId: number, cantidad: number): void {
    const item = this.items.find(i => i.productoId === productoId);
    if (!item) return;

    if (cantidad <= 0) {
      this.eliminarProducto(productoId);
    } else {
      item.cantidad = cantidad;
      this.guardarEnStorage();
    }
  }

  /** Elimina un producto del carrito */
  eliminarProducto(productoId: number): void {
    this.items = this.items.filter(i => i.productoId !== productoId);
    this.guardarEnStorage();
  }

  /** Vacía completamente el carrito */
  vaciar(): void {
    this.items = [];
    this.guardarEnStorage();
  }
}
