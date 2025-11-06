import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-nuevo-producto',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './nuevo-producto.html',
  styleUrls: ['./nuevo-producto.scss']
})
export class NuevoProducto {
  categorias = ['Moda y Accesorios', 'Tecnología', 'Artesanías', 'Salud y Belleza', 'Hogar y Cocina'];
  producto: any = {};

  guardarProducto() {
    console.log('Producto guardado:', this.producto);
    alert('✅ Producto agregado correctamente');
    this.producto = {};
  }
}

