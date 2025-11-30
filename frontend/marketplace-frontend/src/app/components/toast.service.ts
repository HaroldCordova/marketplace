import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ToastService {
  constructor() { }

  success(message: string, title?: string) {
    console.log('[Toast] SUCCESS:', title ? `${title} — ${message}` : message);
    // aquí podrías disparar un evento o usar MatSnackBar/SweetAlert
  }

  error(message: string, title?: string) {
    console.error('[Toast] ERROR:', title ? `${title} — ${message}` : message);
  }

  info(message: string, title?: string) {
    console.info('[Toast] INFO:', title ? `${title} — ${message}` : message);
  }

  warn(message: string, title?: string) {
    console.warn('[Toast] WARN:', title ? `${title} — ${message}` : message);
  }
}
