import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { PersonalInfoComponent } from './form/personal-info/personal-info.component'; 
import { OfficialDocumentsComponent } from './form/official-documents/official-documents.component'; 
import { AcademicInfoComponent } from './form/academic-info/academic-info.component'; 
import { ContactInfoComponent } from './form/contact-info/contact-info.component'; 
import { StepperComponent } from './stepper/stepper.component'; 

@NgModule({
  declarations: [
    AppComponent,
    PersonalInfoComponent,
    OfficialDocumentsComponent, 
    AcademicInfoComponent, 
    ContactInfoComponent, 
    StepperComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }