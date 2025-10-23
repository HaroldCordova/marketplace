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
email: string = '';
  password: string = '';

  onSubmit() {
    if (this.email === 'Harold20' && this.password === '2020') {
      alert(' Bienvenido administrador Orion');
    } else {
      alert(' Credenciales incorrectas');
    }
  }
}
