import { Component, OnInit } from '@angular/core';
import { CommonModule, NgIf, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { TiendaService, Tienda } from '../../../services/tienda';
import { AuthService } from '../../../services/auth.service';
import { VendedorService, VendedorDashboard } from '../../../services/vendedor';
import { PagoService, PagoCliente } from '../../../services/pago';

@Component({
  selector: 'app-vendedor-dashboard',
  standalone: true,
  imports: [CommonModule, NgIf, NgFor, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard1 implements OnInit {

  nombreVendedor = '';

  tienda: Tienda | null = null;
  cargandoTienda = true;

  resumen: VendedorDashboard | null = null;
  cargandoResumen = true;

  pagosPendientes: PagoCliente[] = [];
  cargandoPagos = true;

  constructor(
    private auth: AuthService,
    private tiendaService: TiendaService,
    private vendedorService: VendedorService,
    private pagoService: PagoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.nombreVendedor = this.auth.nombre ?? 'Vendedor';

    const idVendedor = this.auth.userId;
    if (!idVendedor) {
      this.cargandoTienda = false;
      this.cargandoResumen = false;
      this.cargandoPagos = false;
      return;
    }

    this.cargarTienda(idVendedor);
    this.cargarResumen();
    this.cargarPagosPendientes();
  }

  private cargarTienda(idVendedor: number) {
    this.tiendaService.obtenerPorVendedor(idVendedor).subscribe({
      next: (tienda) => {
        this.tienda = tienda;
        this.cargandoTienda = false;
      },
      error: () => {
        this.tienda = null;
        this.cargandoTienda = false;
      }
    });
  }

  private cargarResumen() {
    this.vendedorService.obtenerDashboard().subscribe({
      next: (res) => {
        this.resumen = res;
        this.cargandoResumen = false;
      },
      error: () => {
        this.resumen = null;
        this.cargandoResumen = false;
      }
    });
  }

  private cargarPagosPendientes() {
    this.pagoService.getPagosComoVendedor().subscribe({
      next: (pagos) => {
        // solo pagos RETENIDOS para mostrar en la tabla
        this.pagosPendientes = pagos.filter(p => p.estado === 'retenido');
        this.cargandoPagos = false;
      },
      error: () => {
        this.pagosPendientes = [];
        this.cargandoPagos = false;
      }
    });
  }

  confirmarPago(pago: PagoCliente) {
    this.pagoService.confirmarVendedor(pago.id).subscribe({
      next: () => {
        pago.confirmadoVendedor = true;
        // si quieres sacarlo de la lista:
        // this.pagosPendientes = this.pagosPendientes.filter(p => p.id !== pago.id);
        this.cargarResumen(); // refresca "pagos pendientes"
      }
    });
  }

  irAProductos() {
    this.router.navigate(['/vendedor/productos']);
  }

  irANuevoProducto() {
    this.router.navigate(['/vendedor/nuevo']);
  }

  irAFormalizate() {
    this.router.navigate(['/vendedor/formalizate']);
  }
}
