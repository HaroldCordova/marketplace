import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';
import { FormalizateComponent} from '../formalizate/formalizate'; 

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet,FormalizateComponent],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard1 {
 nombreVendedor = 'Juan Pérez';
  ventasHoy = 5;
  ingresos = 820.50;
  menu = [
    { icono: 'bi bi-house-door', titulo: 'Inicio', ruta: '/vendedor/dashboard' },
    { icono: 'bi bi-box', titulo: 'Mis Productos', ruta: '/vendedor/productos' },
    { icono: 'bi bi-bag-check', titulo: 'Pedidos', ruta: '/vendedor/pedidos' },
    { icono: 'bi bi-person', titulo: 'Perfil', ruta: '/vendedor/perfil' },
    { icono: 'bi bi-journal-text', titulo: 'Formalízate', ruta: '/vendedor/formalizate' },
  ];
}
