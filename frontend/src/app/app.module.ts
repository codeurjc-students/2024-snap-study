import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { FooterComponent } from './components/childs/footer.component';
import { LoginComponent } from './components/auth/login.component';
import { HeaderComponent } from './components/childs/header.component';
import { SignupComponent } from './components/signup/signup.component';
import { MainComponent } from './components/main/main.component';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { DegreeListComponent } from './components/degrees-list/degree-list.component';
import { SubjectListComponent } from './components/subjects-list/subject-list.component';


@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    LoginComponent,
    HeaderComponent,
    SignupComponent,
    MainComponent,
    DegreeListComponent,
    SubjectListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    HttpClientModule,
    CommonModule
  ],
  providers: [],
  bootstrap: [AppComponent],

})
export class AppModule { }