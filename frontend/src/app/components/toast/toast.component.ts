import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { NotificationService, ToastMessage } from '../../services/notification.service';

@Component({
  selector: 'app-toast-container',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css']
})
export class ToastComponent implements OnInit, OnDestroy {
  toasts: ToastMessage[] = [];
  private sub?: Subscription;

  constructor(private notifications: NotificationService) {}

  ngOnInit(): void {
    this.sub = this.notifications.toasts$.subscribe(list => this.toasts = list);
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  close(id: number): void {
    this.notifications.dismiss(id);
  }
} 