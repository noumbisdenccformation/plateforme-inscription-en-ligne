import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private authSubject = new BehaviorSubject<boolean>(false);
  isAuthenticated$: Observable<boolean> = this.authSubject.asObservable();

  login(token: string): void {
    localStorage.setItem('auth_token', token);
    this.authSubject.next(true);
  }

  logout(): void {
    localStorage.removeItem('auth_token');
    this.authSubject.next(false);
  }

  get isAuthenticated(): boolean {
    return !!localStorage.getItem('auth_token');
  }
} 