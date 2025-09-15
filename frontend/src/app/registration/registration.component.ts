import { Component } from '@angular/core';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  currentStep = 1;

  goToNextStep(): void {
    if (this.currentStep < 5) {
      this.currentStep++;
    }
  }
} 