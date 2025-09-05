import { Component, OnInit, Output, EventEmitter } from '@angular/core'; 
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';

@Component({
  selector: 'app-personal-info',
  templateUrl: './personal-info.component.html',
  styleUrls: ['./personal-info.component.css']
})
export class PersonalInfoComponent implements OnInit {
  @Output() nextStep = new EventEmitter<void>(); // 

  personalInfoForm!: FormGroup;

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.personalInfoForm = this.fb.group({
      nom: ['', [Validators.required, Validators.pattern(/^[a-zA-Z\s]*$/)]], // Validation: uniquement lettres et espaces
      prenom: ['', Validators.required],
      sexe: ['', Validators.required],
      dateNaissance: ['', [Validators.required, this.minAgeValidator(16)]], // Validation: âge minimum 16 ans
      nationalite: ['', Validators.required]
    });
  }

  // Validateur personnalisé pour l'âge
  minAgeValidator(minAge: number) {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null; // Pas de validation si le champ est vide
      }

      const birthDate = new Date(control.value);
      const today = new Date();
      let age = today.getFullYear() - birthDate.getFullYear();
      const m = today.getMonth() - birthDate.getMonth();

      if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }

      return age >= minAge ? null : { minAge: true };
    };
  }

  onSubmit(): void {
    if (this.personalInfoForm.valid) {
             this.nextStep.emit(); // <-- Emettez l'événement
    } else {
      console.log('Formulaire invalide.');
    }
  }
}