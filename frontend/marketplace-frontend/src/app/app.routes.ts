import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { Login } from './components/login/login';
import { Registro } from './components/registro/registro';
import { Carrito } from './components/carrito/carrito';
import { ProductoDetalle } from './components/producto-detalle/producto-detalle';
import { Marketplace } from './components/marketplace/marketplace';
import { MisPedidos } from './components/mis-pedidos/mis-pedidos';
import { Dashboard1 } from './components/vendedor/dashboard/dashboard';
import { Dashboard } from './components/admin/dashboard/dashboard';
import { FormalizateComponent } from './components/vendedor/formalizate/formalizate';
import { ProductosVendedor } from './components/vendedor/productos/productos';
import { NuevoProducto } from './components/vendedor/nuevo-producto/nuevo-producto';

// Guards
import { AdminGuard } from './guards/admin.guard';
import { VendedorGuard } from './guards/vendedor.guard';
import { CompradorGuard } from './guards/comprador.guard';

export const routes: Routes = [
  // 🏠 Home público
  { path: '', component: Home },

  // 🔐 Auth
  { path: 'login', component: Login },
  { path: 'registro', component: Registro },

  // 🛍 Catálogo público (cualquiera puede ver productos)
  { path: 'marketplace', component: Marketplace },
  { path: 'producto/:id', component: ProductoDetalle },
  { path: 'carrito', component: Carrito },

  // 🧾 Solo comprador logueado
  { path: 'mis-pedidos', component: MisPedidos, canActivate: [CompradorGuard] },

  // 🧰 Solo vendedor
  { path: 'vendedor/panel', component: Dashboard1, canActivate: [VendedorGuard] },
  { path: 'vendedor/formalizate', component: FormalizateComponent, canActivate: [VendedorGuard] },
  { path: 'vendedor/productos', component: ProductosVendedor, canActivate: [VendedorGuard] },
  { path: 'vendedor/nuevo', component: NuevoProducto, canActivate: [VendedorGuard] },

  // 👑 Solo admin
  { path: 'admin/panel', component: Dashboard, canActivate: [AdminGuard] },

  // 404 -> home
  { path: '**', redirectTo: '' }
];
