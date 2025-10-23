import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { Login } from './components/login/login';
import { Marketplace } from './components/marketplace/marketplace';
import { Carrito } from './components/carrito/carrito';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'marketplace', component: Marketplace },
  { path: 'carrito', component: Carrito },
];
