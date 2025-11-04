import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { Login } from './components/login/login';
import { Registro } from './components/registro/registro';
import { Carrito } from './components/carrito/carrito';
import { ProductoDetalle } from './components/producto-detalle/producto-detalle';
import { Marketplace } from './components/marketplace/marketplace';

// Dashboards
import { Dashboard1 } from './components/vendedor/dashboard/dashboard';
import { Dashboard } from './components/admin/dashboard/dashboard';
import { FormalizateComponent } from './components/vendedor/formalizate/formalizate'; // ✅ CORREGIDO

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'registro', component: Registro },
  { path: 'carrito', component: Carrito },
  { path: 'marketplace', component: Marketplace },
  { path: 'producto/:id', component: ProductoDetalle },
  { path: 'vendedor/panel', component: Dashboard1 },
  { path: 'vendedor/formalizate', component: FormalizateComponent }, 
  { path: 'admin/panel', component: Dashboard },
  { path: '**', redirectTo: '' }
];
