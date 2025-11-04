import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-formalizate',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './formalizate.html',
  styleUrl: './formalizate.scss'
})
export class FormalizateComponent {
pasos = [
    {
      titulo: 'Crear cuenta en la SUNAT',
      descripcion: 'Accede al portal y regístrate para obtener tu clave SOL.',
      link: 'https://www.sunat.gob.pe/',
      completado: false
    },
    {
      titulo: 'Obtener RUC',
      descripcion: 'Registra tu negocio en la SUNAT y obtén tu número de RUC.',
      link: 'https://www.sunat.gob.pe/ol-ti-itinsrucsol/',
      completado: false
    },
    {
      titulo: 'Registrar tu marca',
      descripcion: 'Registra el nombre de tu negocio en INDECOPI.',
      link: 'https://servicios.indecopi.gob.pe/',
      completado: false
    },
    {
      titulo: 'Emitir boletas electrónicas',
      descripcion: 'Aprende cómo emitir comprobantes de pago electrónicos.',
      link: 'https://orientacion.sunat.gob.pe/',
      completado: false
    }
  ];

  togglePaso(paso: any) {
    paso.completado = !paso.completado;
  }

  get progreso() {
    const completados = this.pasos.filter(p => p.completado).length;
    return Math.round((completados / this.pasos.length) * 100);
  }
}
