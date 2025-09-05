import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-stepper',
  templateUrl: './stepper.component.html',
  styleUrls: ['./stepper.component.css']
})
export class StepperComponent {
  @Input() currentStep: number = 1; // La propriété pour l'étape actuelle

  steps = [
    'Informations Personnelles',
    'Documents Officiels',
    'Parcours Académique',
    'Coordonnées',
    'Récapitulatif & Paiement'
  ];
}