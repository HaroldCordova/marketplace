import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoaderService } from '../loader.service';

@Component({
  selector: 'app-loader',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="loader-backdrop" *ngIf="loader.loading$ | async">
      <div class="spinner-border text-primary" role="status"></div>
    </div>
  `,
  styles: [`
    .loader-backdrop {
      position: fixed; inset: 0; background: rgba(255,255,255,.6);
      display: grid; place-items: center; z-index: 1070;
      backdrop-filter: blur(2px);
    }
  `]
})
export class LoaderComponent {
  constructor(public loader: LoaderService) {}
}
