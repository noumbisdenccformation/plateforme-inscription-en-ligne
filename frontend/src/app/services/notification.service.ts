import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface ToastMessage {
  type: 'info' | 'success' | 'warning' | 'error';
  text: string;
  id: number;
  timeoutMs?: number;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private toastsSubject = new BehaviorSubject<ToastMessage[]>([]);
  toasts$ = this.toastsSubject.asObservable();
  private counter = 0;

  show(text: string, type: ToastMessage['type'] = 'info', timeoutMs = 3000): void {
    const id = ++this.counter;
    const toast: ToastMessage = { id, text, type, timeoutMs };
    const current = this.toastsSubject.getValue();
    this.toastsSubject.next([...current, toast]);
    if (timeoutMs > 0) {
      setTimeout(() => this.dismiss(id), timeoutMs);
    }
  }

  dismiss(id: number): void {
    const current = this.toastsSubject.getValue();
    this.toastsSubject.next(current.filter(t => t.id !== id));
  }

  clear(): void {
    this.toastsSubject.next([]);
  }
} 