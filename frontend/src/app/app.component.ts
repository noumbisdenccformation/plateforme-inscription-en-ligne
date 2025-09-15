import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'plateforme-inscription-frontend';
  currentStep = 1; // Initialisez l'étape actuelle à 1

  // Méthode pour passer à l'étape suivante
  goToNextStep(): void {
    if (this.currentStep < 5) {
      this.currentStep++;
    }
  }
}