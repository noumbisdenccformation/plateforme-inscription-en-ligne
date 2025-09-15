import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AutoSaveService } from '../../services/autosave.service';
import { debounceTime } from 'rxjs';

@Component({
  selector: 'app-academic-info',
  templateUrl: './academic-info.component.html',
  styleUrls: ['./academic-info.component.css']
})
export class AcademicInfoComponent implements OnInit {
  @Output() nextStep = new EventEmitter<void>();

  academicForm!: FormGroup;

  constructor(private formBuilder: FormBuilder, private autoSave: AutoSaveService) {}

  ngOnInit(): void {
    this.academicForm = this.formBuilder.group({
      lastSchool: ['', Validators.required],
      specialization: ['', Validators.required],
      periodStart: ['', Validators.required],
      periodEnd: ['', Validators.required]
    });

    const cached = this.autoSave.load<any>('step_academic');
    if (cached) {
      this.academicForm.patchValue(cached);
    }

    this.academicForm.valueChanges.pipe(debounceTime(300)).subscribe(v => this.autoSave.save('step_academic', v));
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
    this.autoSave.save('step_academic', this.academicForm.value);
    this.nextStep.emit();
  }
}
