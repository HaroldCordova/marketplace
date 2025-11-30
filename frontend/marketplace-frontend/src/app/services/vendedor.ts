import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

export interface VendedorDashboard {
  ventasMes: number;
  productosPublicados: number;
  pedidosPendientes: number;
  enviosPendientes: number;
}

@Injectable({ providedIn: 'root' })
export class VendedorService {

  private apiUrl = `${environment.apiBaseUrl}/api/vendedor`;

  constructor(
    private http: HttpClient,
    private auth: AuthService
  ) {}

  private buildOptions() {
    const token =
      (this.auth as any).token ??
      (this.auth as any).getToken?.();
    return token
      ? { headers: new HttpHeaders().set('Authorization', 'Bearer ' + token) }
      : {};
  }

  obtenerDashboard(): Observable<VendedorDashboard> {
    return this.http.get<VendedorDashboard>(
      `${this.apiUrl}/dashboard`,
      this.buildOptions()
    );
  }
}
