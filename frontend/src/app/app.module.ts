import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { PersonalInfoComponent } from './form/personal-info/personal-info.component'; 
import { OfficialDocumentsComponent } from './form/official-documents/official-documents.component'; 
import { AcademicInfoComponent } from './form/academic-info/academic-info.component'; 
import { ContactInfoComponent } from './form/contact-info/contact-info.component'; 
import { StepperComponent } from './stepper/stepper.component'; 
import { RecapPaymentComponent } from './form/recap-payment/recap-payment.component';
import { ToastComponent } from './components/toast/toast.component';
import { AppRoutingModule } from './app-routing.module';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { RegistrationComponent } from './registration/registration.component';
import { DashboardComponent } from './admin/dashboard/dashboard.component';
import { DossiersComponent } from './admin/dossiers/dossiers.component';

@NgModule({
  declarations: [
    AppComponent,
    PersonalInfoComponent,
    OfficialDocumentsComponent, 
    AcademicInfoComponent, 
    ContactInfoComponent, 
    StepperComponent,
    RecapPaymentComponent,
    ToastComponent,
    LandingPageComponent,
    LoginPageComponent,
    RegistrationComponent,
    DashboardComponent,
    DossiersComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }