import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export type ToastLevel = 'success' | 'info' | 'warning' | 'danger';

export interface Toast {
  id: number;
  level: ToastLevel;
  message: string;
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private _toasts = new Subject<Toast>();
  toasts$ = this._toasts.asObservable();
  private id = 0;

  show(level: ToastLevel, message: string) {
    this._toasts.next({ id: ++this.id, level, message });
  }

  success(m: string)  { this.show('success', m); }
  info(m: string)     { this.show('info', m); }
  warn(m: string)     { this.show('warning', m); }

  // 👇 Alias para que puedas usar this.toast.warning(...)
  warning(m: string)  { this.show('warning', m); }

  error(m: string)    { this.show('danger', m); }
}
