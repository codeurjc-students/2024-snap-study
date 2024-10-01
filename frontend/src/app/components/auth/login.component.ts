import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {

  public email: string = '';
  public password: string = '';

  constructor(private authService: AuthService, private router: Router){}

  sendCredentials(event: Event) {
    event.preventDefault();

    this.authService.login(this.email, this.password).subscribe({
      next: () => {
        this.router.navigate(['/']);
     },
      error: (err: HttpErrorResponse) => {
        console.log("S");
       },
     });
  }

}