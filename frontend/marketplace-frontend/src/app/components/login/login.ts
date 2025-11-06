import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../shared/toast.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class Login {
  email = '';
  password = '';
  rolSeleccionado: 'comprador' | 'vendedor' | 'admin' | '' = '';

  constructor(private router: Router, private auth: AuthService, private toast: ToastService) {}

  iniciarSesion() {
    if (!this.email || !this.password || !this.rolSeleccionado) {
      this.toast.warn('Completa todos los campos y selecciona tu rol.');
      return;
    }

    // ✅ Guardar usuario y rol simulado
    this.auth.login(this.email, this.rolSeleccionado);

    switch (this.rolSeleccionado) {
      case 'admin':
        this.toast.success('Bienvenido, Administrador 👑');
        this.router.navigate(['/admin/panel']);
        break;

      case 'vendedor':
        this.toast.success('Bienvenido, Vendedor 🧰');
        this.router.navigate(['/vendedor/productos']);
        break;

      case 'comprador':
        this.toast.success('Bienvenido, Comprador 🛒');
        this.router.navigate(['/marketplace']);
        break;
    }
  }
}
