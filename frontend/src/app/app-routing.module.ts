import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import { RegistrationComponent } from './registration/registration.component';
import { DashboardComponent } from './admin/dashboard/dashboard.component';
import { DossiersComponent } from './admin/dossiers/dossiers.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'inscription', component: RegistrationComponent },
  { path: 'admin', canActivate: [AuthGuard], children: [
    { path: 'dashboard', component: DashboardComponent },
    { path: 'dossiers', component: DossiersComponent },
    { path: '', pathMatch: 'full', redirectTo: 'dashboard' }
  ]},
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {} 