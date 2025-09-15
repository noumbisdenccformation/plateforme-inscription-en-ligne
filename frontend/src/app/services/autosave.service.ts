import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AutoSaveService {
  save<T>(key: string, value: T): void {
    try { localStorage.setItem(key, JSON.stringify(value)); } catch {}
  }
  load<T>(key: string): T | null {
    try {
      const raw = localStorage.getItem(key);
      return raw ? JSON.parse(raw) as T : null;
    } catch {
      return null;
    }
  }
  remove(key: string): void {
    try { localStorage.removeItem(key); } catch {}
  }
} 