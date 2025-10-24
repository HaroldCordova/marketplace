import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CarritoService {
private carrito: any[] = [];
private contador = new BehaviorSubject<number>(0);

// Observable al que se pueden suscribir otros componentes
  contador$ = this.contador.asObservable();

  obtenerCarrito() {
    return this.carrito;
  }

  agregarProducto(producto: any) {
    const existente = this.carrito.find(p => p.nombre === producto.nombre);
    if (existente) {
      existente.cantidad += 1;
    } else {
      this.carrito.push({ ...producto, cantidad: 1 });
    }
    this.actualizarContador();
  }

  eliminarProducto(index: number) {
    this.carrito.splice(index, 1);
    this.actualizarContador();
  }

  limpiarCarrito() {
    this.carrito = [];
    this.actualizarContador();
  }
  
   private actualizarContador() {
    const total = this.carrito.reduce((sum, item) => sum + item.cantidad, 0);
    this.contador.next(total);
  }
}
