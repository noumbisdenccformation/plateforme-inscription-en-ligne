import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-contact-info',
  templateUrl: './contact-info.component.html',
  styleUrls: ['./contact-info.component.css']
})
export class ContactInfoComponent implements OnInit {
  @Output() nextStep = new EventEmitter<void>();

  contactForm!: FormGroup;

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.contactForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      emailConfirm: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]],
      address: ['', Validators.required],
      emergencyName: ['', Validators.required],
      emergencyPhone: ['', Validators.required]
    });

    const cached = localStorage.getItem('step_contact');
    if (cached) {
      try { this.contactForm.patchValue(JSON.parse(cached)); } catch {}
    }
  }

  onSubmit(): void {
    if (this.contactForm.invalid) {
      this.contactForm.markAllAsTouched();
      return;
    }
    if (this.contactForm.value.email !== this.contactForm.value.emailConfirm) {
      this.contactForm.get('emailConfirm')?.setErrors({ mismatch: true });
      return;
    }
    localStorage.setItem('step_contact', JSON.stringify(this.contactForm.value));
    this.nextStep.emit();
  }
}
