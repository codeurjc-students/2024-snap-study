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
import { ProfileComponent } from './components/profile/profile.component';
import { DocumentListComponent } from './components/documents-list/document-list.component';
import { AdminPannelComponent } from './components/admin/admin-pannel.component';
import { PopUpDialogComponent } from './components/childs/popup-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { PopUpImageComponent } from './components/childs/popup-image.component';
import { AdminSubjectsComponent } from './components/admin/admin-subjects.component';
import { AdminDocumentsComponent } from './components/admin/admin-documents.component';



@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    LoginComponent,
    HeaderComponent,
    SignupComponent,
    MainComponent,
    DegreeListComponent,
    SubjectListComponent,
    ProfileComponent,
    DocumentListComponent,
    AdminPannelComponent,
    PopUpDialogComponent,
    PopUpImageComponent,
    AdminSubjectsComponent,
    AdminPannelComponent,
    AdminDocumentsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    CommonModule,
    HttpClientModule,
    MatDialogModule
  ],
  providers: [],
  bootstrap: [AppComponent],

})
export class AppModule { }