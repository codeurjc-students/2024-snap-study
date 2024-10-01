import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',

})
export class SignupComponent {
  public firstName: string;
  public lastName: string;
  public email: string;
  public password: string;
  public password2: string;

  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) {
    this.firstName = '';
    this.lastName = '';
    this.email = '';
    this.password = '';
    this.password2 = '';
  }

  createUser() {
    if (this.password != this.password2) {
      console.log('Passwords do not match');
      this.router.navigate(['error']);
    } else {
      this.authService.createUser(this.firstName, this.lastName, this.email, this.password).subscribe({
        next: _ => {
          { this.router.navigate(['/login']); }
        },
        error: (err: HttpErrorResponse) => {
          if (err.status === 400) {
            console.log('Fill all the fields');
            this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
              this.router.navigate(['/signup']);
            });
          } else {
            this.router.navigate(['/error']);
          }
        }
      });
    }


  }

}