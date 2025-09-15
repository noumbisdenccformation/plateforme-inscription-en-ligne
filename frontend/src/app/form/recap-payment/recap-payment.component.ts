import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-recap-payment',
  templateUrl: './recap-payment.component.html',
  styleUrls: ['./recap-payment.component.css']
})
export class RecapPaymentComponent implements OnInit {
  personal: any;
  documents: any;
  academic: any;
  contact: any;

  submitted = false;

  ngOnInit(): void {
    try { this.personal = JSON.parse(localStorage.getItem('step_personal') || 'null'); } catch { this.personal = null; }
    try { this.documents = JSON.parse(localStorage.getItem('step_documents') || 'null'); } catch { this.documents = null; }
    try { this.academic = JSON.parse(localStorage.getItem('step_academic') || 'null'); } catch { this.academic = null; }
    try { this.contact = JSON.parse(localStorage.getItem('step_contact') || 'null'); } catch { this.contact = null; }
  }

  submitAll(): void {
    this.submitted = true;
  }
} 