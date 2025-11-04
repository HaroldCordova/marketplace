import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

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
  rolSeleccionado = 'comprador';

  constructor(private router: Router) {}

  iniciarSesion() {
    console.log(`Rol seleccionado: ${this.rolSeleccionado}`);

    // Simulación de autenticación
    if (this.rolSeleccionado === 'admin') {
      this.router.navigate(['/admin/dashboard']);
    } else if (this.rolSeleccionado === 'vendedor') {
      this.router.navigate(['/vendedor/dashboard']);
    } else {
      this.router.navigate(['/marketplace']);
    }
  }
}
