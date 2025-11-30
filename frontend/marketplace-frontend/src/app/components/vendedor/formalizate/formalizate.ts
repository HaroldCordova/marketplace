// src/app/components/vendedor/formalizate/formalizate.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TiendaService, Tienda } from '../../../services/tienda';
import { AuthService } from '../../../services/auth.service';
import { ToastService } from '../../shared/toast.service';
import {
  FormalizacionService,
  PasoFormalizacion
} from '../../../services/formalizacion';

// Lo que se muestra en UI: DTO + texto + link
type PasoFormalizacionUI = PasoFormalizacion & {
  descripcion?: string;
  link?: string;
};

@Component({
  selector: 'app-formalizate',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './formalizate.html',
  styleUrl: './formalizate.scss'
})
export class FormalizateComponent implements OnInit {

  // ===== TIENDA =====
  cargandoTienda = true;
  tienda: Tienda | null = null;

  nombreTienda = '';
  descripcion = '';
  ruc = '';

  // ===== FORMALIZACIÓN =====
  pasos: PasoFormalizacionUI[] = [];
  cargandoPasos = false;

  // Mapa: nombre del paso -> descripción + link
  private pasosMeta: Record<string, { descripcion: string; link: string }> = {
    'Crear cuenta en la SUNAT': {
      descripcion: 'Accede al portal y regístrate para obtener tu clave SOL.',
      link: 'https://www.sunat.gob.pe/'
    },
    'Obtener RUC': {
      descripcion: 'Registra tu negocio en la SUNAT y obtén tu número de RUC.',
      link: 'https://www.gob.pe/284-inscripcion-en-el-ruc'
    },
    'Registrar tu marca': {
      descripcion: 'Registra el nombre de tu negocio en INDECOPI.',
      link: 'https://www.gob.pe/333-registrar-una-marca-registrar-marca-de-producto-y-o-servicio'
    },
    'Emitir boletas electrónicas': {
      descripcion: 'Aprende cómo emitir comprobantes de pago electrónicos.',
      link: 'https://cpe.sunat.gob.pe/videos'
    }
  };

  constructor(
    private tiendaService: TiendaService,
    private auth: AuthService,
    private toast: ToastService,
    private formalizacionService: FormalizacionService
  ) {}

  ngOnInit(): void {
    this.cargarTienda();
    this.cargarPasos();
  }

  // ================= TIENDA =================

  private cargarTienda(): void {
    const idVendedor = this.auth.userId;

    if (!idVendedor) {
      this.toast.error('No se encontró la sesión del vendedor.');
      this.cargandoTienda = false;
      return;
    }

    this.tiendaService.obtenerPorVendedor(idVendedor).subscribe({
      next: (tienda) => {
        this.tienda = tienda;
        this.nombreTienda = tienda.nombre;
        this.descripcion = tienda.descripcion ?? '';
        this.ruc = tienda.ruc ?? '';
        this.cargandoTienda = false;
      },
      error: () => {
        this.tienda = null;
        this.cargandoTienda = false;
      }
    });
  }

  crearTienda(): void {
    const idVendedor = this.auth.userId;
    if (!idVendedor) {
      this.toast.error('No se encontró la sesión del vendedor.');
      return;
    }

    if (!this.nombreTienda.trim()) {
      this.toast.warn('Ingresa el nombre de la tienda.');
      return;
    }

    const body = {
      idVendedor,
      nombre: this.nombreTienda.trim(),
      descripcion: this.descripcion.trim(),
      ruc: this.ruc.trim() || null
    };

    this.tiendaService.crearTienda(body).subscribe({
      next: (tienda) => {
        this.tienda = tienda;
        this.toast.success('Tienda creada correctamente.');
      },
      error: (err) => {
        console.error(err);
        this.toast.error('No se pudo crear la tienda.');
      }
    });
  }

  actualizarTienda(): void {
    if (!this.tienda) return;

    if (!this.nombreTienda.trim()) {
      this.toast.warn('Ingresa el nombre de la tienda.');
      return;
    }

    const body = {
      idVendedor: this.tienda.idVendedor,
      nombre: this.nombreTienda.trim(),
      descripcion: this.descripcion.trim(),
      ruc: this.ruc.trim() || null
    };

    this.tiendaService.actualizarTienda(this.tienda.id, body).subscribe({
      next: (tienda) => {
        this.tienda = tienda;
        this.toast.success('Tienda actualizada correctamente.');
      },
      error: (err) => {
        console.error(err);
        this.toast.error('No se pudo actualizar la tienda.');
      }
    });
  }

  // ================= FORMALIZACIÓN =================

  private cargarPasos(): void {
    this.cargandoPasos = true;

    this.formalizacionService.getMisPasos().subscribe({
      next: (lista) => {
        this.pasos = lista.map(p => ({
          ...p,
          ...(this.pasosMeta[p.paso] ?? {})
        }));
        this.cargandoPasos = false;
      },
      error: (err) => {
        console.error(err);
        this.cargandoPasos = false;
      }
    });
  }

  togglePaso(paso: PasoFormalizacionUI): void {
    const nuevoEstado = !paso.completado;

    this.formalizacionService.actualizarPaso(paso.id, nuevoEstado).subscribe({
      next: (actualizado) => {
        paso.completado = actualizado.completado;
        paso.fechaActualizacion = actualizado.fechaActualizacion;

        this.toast.success(
          nuevoEstado
            ? `Marcaste "${paso.paso}" como completado.`
            : `Marcaste "${paso.paso}" como pendiente.`
        );
      },
      error: () => {
        this.toast.error('No se pudo actualizar el paso de formalización.');
      }
    });
  }

  get progreso(): number {
    if (!this.pasos.length) return 0;
    const completados = this.pasos.filter(p => p.completado).length;
    return Math.round((completados / this.pasos.length) * 100);
  }
}
