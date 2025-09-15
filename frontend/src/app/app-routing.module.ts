import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { RegistrationComponent } from './registration/registration.component';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'inscription', component: RegistrationComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {} 