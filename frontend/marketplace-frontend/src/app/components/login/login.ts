import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';        // 👈 AÑADIR ESTO
import { AuthService, LoginResponse } from '../../services/auth.service';
import { ToastService } from '../shared/toast.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule                     // 👈 Y AÑADIRLO AQUÍ
  ],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class Login {
  email = '';
  password = '';

  constructor(
    private router: Router,
    private auth: AuthService,
    private toast: ToastService
  ) {}

  iniciarSesion() {
    if (!this.email || !this.password) {
      this.toast.warn('Ingresa tu correo y contraseña.');
      return;
    }

    this.auth.login(this.email, this.password).subscribe({
      next: (resp: LoginResponse) => {
        this.auth.saveSession(resp);

        switch (resp.rol) {
          case 'admin':
            this.router.navigate(['/admin/panel']);
            break;
          case 'vendedor':
            this.router.navigate(['/vendedor/panel']);
            break;
          case 'comprador':
          default:
            this.router.navigate(['/marketplace']);
            break;
        }
      },
      error: () => {
        this.toast.error('Credenciales incorrectas o cuenta inactiva.');
      }
    });
  }
}
