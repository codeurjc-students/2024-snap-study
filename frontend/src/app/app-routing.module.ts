import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { MainComponent } from './components/main/main.component';
import { ErrorComponent } from './components/errors/error.component';
import { DegreeListComponent } from './components/degrees-list/degree-list.component';
import { SubjectListComponent } from './components/subjects-list/subject-list.component';

const routes: Routes = [
  { path: '', component: MainComponent },
  { path: 'login', component: LoginComponent },
  { path: 'error', component: ErrorComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'degrees', component: DegreeListComponent},
  { path: 'degrees/:id', component: SubjectListComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }