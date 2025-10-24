import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { Login } from './components/login/login';
import { Marketplace } from './components/marketplace/marketplace';
import { Carrito } from './components/carrito/carrito';
import { ProductoDetalle } from './components/producto-detalle/producto-detalle';


export const routes: Routes = [
  { path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'marketplace', component: Marketplace },
  { path: 'carrito', component: Carrito },
  { path: 'producto/:id', component: ProductoDetalle }
];
