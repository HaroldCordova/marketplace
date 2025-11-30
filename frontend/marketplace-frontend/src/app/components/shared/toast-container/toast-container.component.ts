import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription, timer } from 'rxjs';
import { Toast, ToastService } from '../toast.service';

@Component({
  selector: 'app-toast-container',
  standalone: true,
  imports: [CommonModule],
  template: `
  <div class="toast-stack position-fixed top-0 end-0 p-3" style="z-index:1080">
    <div *ngFor="let t of toasts" class="toast-item alert alert-{{t.level}} shadow mb-2" role="alert">
      {{ t.message }}
    </div>
  </div>`,
  styles: [`
    .toast-item { border-radius: 10px; animation: fadeSlide .2s ease; }
    @keyframes fadeSlide { from {opacity:0; transform: translateY(-6px)} to {opacity:1; transform: translateY(0)} }
  `]
})
export class ToastContainerComponent implements OnDestroy {
  toasts: Toast[] = [];
  sub?: Subscription;

  constructor(private toast: ToastService) {
    this.sub = this.toast.toasts$.subscribe(t => {
      this.toasts.push(t);
      timer(2200).subscribe(() => this.toasts = this.toasts.filter(x => x.id !== t.id));
    });
  }
  ngOnDestroy() { this.sub?.unsubscribe(); }
}
