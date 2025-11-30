import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';      
import { Router } from '@angular/router';
import { AuthService, LoginResponse } from '../../services/auth.service';
import { ToastService } from '../shared/toast.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule,RouterModule],
  templateUrl: './registro.html',
  styleUrls: ['./registro.scss']
})
export class Registro {

  nombre = '';
  apellido = '';
  correo = '';
  telefono = '';
  direccion = '';
  password = '';
  confirmarPassword = '';
  rol: 'comprador' | 'vendedor' = 'comprador';

  constructor(
    private auth: AuthService,
    private router: Router,
    private toast: ToastService
  ) {}

  registrar() {
    if (!this.nombre || !this.apellido || !this.correo || !this.password || !this.confirmarPassword) {
      this.toast.warn('Completa los campos obligatorios.');
      return;
    }

    if (this.password !== this.confirmarPassword) {
      this.toast.warn('Las contraseñas no coinciden.');
      return;
    }

    const body = {
      nombre: this.nombre,
      apellido: this.apellido,
      correo: this.correo,
      password: this.password,
      telefono: this.telefono || null,
      direccion: this.direccion || null,
      rol: this.rol
    };

    this.auth.register(body).subscribe({
      next: (resp: LoginResponse) => {
        this.auth.saveSession(resp);
        this.toast.success('Cuenta creada correctamente.');

        if (resp.rol === 'vendedor') {
          this.router.navigate(['/vendedor/productos']);
        } else {
          this.router.navigate(['/marketplace']);
        }
      },
      error: (err) => {
        console.error(err);
        this.toast.error('No se pudo registrar la cuenta. Verifica los datos.');
      }
    });
  }
}
