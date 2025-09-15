import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AutoSaveService } from '../../services/autosave.service';
import { debounceTime } from 'rxjs';

@Component({
  selector: 'app-contact-info',
  templateUrl: './contact-info.component.html',
  styleUrls: ['./contact-info.component.css']
})
export class ContactInfoComponent implements OnInit {
  @Output() nextStep = new EventEmitter<void>();

  contactForm!: FormGroup;

  constructor(private formBuilder: FormBuilder, private autoSave: AutoSaveService) {}

  ngOnInit(): void {
    this.contactForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      emailConfirm: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]],
      address: ['', Validators.required],
      emergencyName: ['', Validators.required],
      emergencyPhone: ['', Validators.required]
    });

    const cached = this.autoSave.load<any>('step_contact');
    if (cached) {
      this.contactForm.patchValue(cached);
    }

    this.contactForm.valueChanges.pipe(debounceTime(300)).subscribe(v => this.autoSave.save('step_contact', v));
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
    this.autoSave.save('step_contact', this.contactForm.value);
    this.nextStep.emit();
  }
}
