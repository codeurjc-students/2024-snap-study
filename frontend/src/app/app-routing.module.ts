import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { MainComponent } from './components/main/main.component';
import { ErrorComponent } from './components/errors/error.component';
import { DegreeListComponent } from './components/degrees-list/degree-list.component';
import { SubjectListComponent } from './components/subjects-list/subject-list.component';
import { ProfileComponent } from './components/profile/profile.component';
import { DocumentListComponent } from './components/documents-list/document-list.component';
import { AdminPannelComponent } from './components/admin/admin-pannel.component';
import { AdminSubjectsComponent } from './components/admin/admin-subjects.component';
import { AdminDocumentsComponent } from './components/admin/admin-documents.component';

const routes: Routes = [
  { path: '', component: MainComponent },
  { path: 'login', component: LoginComponent },
  { path: 'error', component: ErrorComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'degrees', component: DegreeListComponent},
  { path: 'degrees/:id', component: SubjectListComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'degrees/:id/subjects/:sid', component: DocumentListComponent },
  { path: 'admin', component: AdminPannelComponent },
  { path: 'admin/degrees/:id', component: AdminSubjectsComponent },
  { path: 'admin/degrees/:id/subjects/:sid', component: AdminDocumentsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }