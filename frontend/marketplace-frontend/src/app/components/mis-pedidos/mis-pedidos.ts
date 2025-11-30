import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PagoService, PagoCliente } from '../../services/pago';
import { ToastService } from '../shared/toast.service';

@Component({
  selector: 'app-mis-pedidos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './mis-pedidos.html',
  styleUrls: ['./mis-pedidos.scss']
})
export class MisPedidos implements OnInit {

  pagos: PagoCliente[] = [];
  cargando = false;

  constructor(
    private pagoService: PagoService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.cargarPagos();
  }

  cargarPagos() {
    this.cargando = true;
    this.pagoService.getPagosComoComprador().subscribe({
      next: data => {
        this.pagos = data;
        this.cargando = false;
      },
      error: err => {
        console.error(err);
        this.cargando = false;
        this.toast.error('No se pudieron cargar tus pedidos.');
      }
    });
  }

  confirmarRecepcion(p: PagoCliente) {
    if (!confirm(`¿Confirmas que recibiste el pedido #${p.pedidoId}?`)) return;

    this.pagoService.confirmarComprador(p.id).subscribe({
      next: () => {
        this.toast.success('Recepción confirmada. ¡Gracias!');
        this.cargarPagos();
      },
      error: err => {
        console.error(err);
        this.toast.error('No se pudo confirmar la recepción.');
      }
    });
  }
}
