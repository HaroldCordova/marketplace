import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './components/navbar/navbar';

// ✅ Importaciones corregidas con la ruta real
import { ToastContainerComponent } from './components/shared/toast-container/toast-container.component';
import { LoaderComponent } from './components/shared/loader/loader.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Navbar, FormsModule, ToastContainerComponent, LoaderComponent],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App {
  protected readonly title = signal('marketplace-frontend');
}
