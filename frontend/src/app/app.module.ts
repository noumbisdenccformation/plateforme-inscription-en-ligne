import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { PersonalInfoComponent } from './form/personal-info/personal-info.component'; // <-- Ajoutez cette ligne

@NgModule({
  declarations: [
    AppComponent,
    PersonalInfoComponent // <-- Ajoutez cette ligne
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }