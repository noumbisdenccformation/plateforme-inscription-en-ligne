import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-academic-info',
  templateUrl: './academic-info.component.html',
  styleUrls: ['./academic-info.component.css']
})
export class AcademicInfoComponent implements OnInit {
  @Output() nextStep = new EventEmitter<void>();

  academicForm!: FormGroup;

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.academicForm = this.formBuilder.group({
      lastSchool: ['', Validators.required],
      specialization: ['', Validators.required],
      periodStart: ['', Validators.required],
      periodEnd: ['', Validators.required]
    });

    const cached = localStorage.getItem('step_academic');
    if (cached) {
      try { this.academicForm.patchValue(JSON.parse(cached)); } catch {}
    }
  }

  onSubmit(): void {
    if (this.academicForm.invalid) {
      this.academicForm.markAllAsTouched();
      return;
    }
    const start = new Date(this.academicForm.value.periodStart);
    const end = new Date(this.academicForm.value.periodEnd);
    if (end < start) {
      this.academicForm.get('periodEnd')?.setErrors({ dateOrder: true });
      return;
    }
    localStorage.setItem('step_academic', JSON.stringify(this.academicForm.value));
    this.nextStep.emit();
  }
}
