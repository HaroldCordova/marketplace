import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Subscription, interval } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { AdminService, ResumenAdmin, PagoAdmin, EnvioAdmin } from '../../../services/admin';
import { ToastService } from '../../shared/toast.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss']
})
export class Dashboard implements OnInit, OnDestroy {

  resumen: ResumenAdmin | null = null;
  cargandoResumen = false;

  pagos: PagoAdmin[] = [];
  cargandoPagos = false;

  envios: EnvioAdmin[] = [];
  cargandoEnvios = false;

  private subs: Subscription[] = [];

  constructor(
    private adminService: AdminService,
    private toast: ToastService
  ) {}

  ngOnInit(): void {
    this.subs.push(this.adminService.getResumen().subscribe(r => {
      this.resumen = r;
    }));

    this.cargarResumen();
    this.cargarPagos();
    this.cargarEnvios();

    const auto = interval(60000).pipe(
      switchMap(() => this.adminService.fetchResumen())
    ).subscribe({
      next: () => {},
      error: (e) => console.error('Auto-refresh error', e)
    });
    this.subs.push(auto);
  }

  cargarResumen(): void {
    this.cargandoResumen = true;
    this.adminService.fetchResumen().subscribe({
      next: (data) => {
        this.resumen = data;
        this.cargandoResumen = false;
      },
      error: (err) => {
        console.error(err);
        this.cargandoResumen = false;
        this.toast.error('No se pudo cargar el resumen del panel.');
      }
    });
  }

  cargarPagos(): void {
    this.cargandoPagos = true;
    this.adminService.getPagosPendientes().subscribe({
      next: (data) => {
        this.pagos = data;
        this.cargandoPagos = false;
      },
      error: (err) => {
        console.error(err);
        this.cargandoPagos = false;
        this.toast.error('No se pudo cargar la lista de pagos.');
      }
    });
  }

  cargarEnvios(): void {
    this.cargandoEnvios = true;
    this.adminService.getEnviosPendientes().subscribe({
      next: (data) => {
        this.envios = data;
        this.cargandoEnvios = false;
      },
      error: (err) => {
        console.error(err);
        this.cargandoEnvios = false;
        this.toast.error('No se pudo cargar la lista de envíos.');
      }
    });
  }

  liberarPago(pago: PagoAdmin): void {
    if (!confirm(`¿Liberar el pago #${pago.id} al vendedor ${pago.vendedorId}?`)) return;

    this.adminService.liberarPago(pago.id).subscribe({
      next: () => {
        this.toast.success('Pago liberado correctamente.');
        this.cargarPagos();
        this.cargarResumen();
      },
      error: (err) => {
        console.error(err);
        this.toast.error('No se pudo liberar el pago.');
      }
    });
  }

  formatearFecha(fechaIso?: string | null): string {
    if (!fechaIso) return '-';
    const d = new Date(fechaIso);
    return d.toLocaleString();
  }

  ngOnDestroy(): void {
    this.subs.forEach(s => s.unsubscribe());
  }
}
