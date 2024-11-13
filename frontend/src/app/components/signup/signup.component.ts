import { Component, Renderer2 } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { PopUpService } from '../../services/popup.service';

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

  constructor(private renderer: Renderer2, private popUpService: PopUpService, private authService: AuthService, private router: Router, private route: ActivatedRoute) {
    this.firstName = '';
    this.lastName = '';
    this.email = '';
    this.password = '';
    this.password2 = '';
  }

  ngOnInit(){
    this.renderer.addClass(document.body, 'search-results-page');
  }

  createUser() {
    if (this.firstName == "" || this.lastName == "") {
      this.popUpService.openPopUp('Some field is incomplete');
    }
    else if (this.password != this.password2 || this.password == "") {
      this.popUpService.openPopUp('Passwords do not match');
    } else {
      this.authService.createUser(this.firstName, this.lastName, this.email, this.password).subscribe({
        next: _ => {
          { this.router.navigate(['/login']); }
        },
        error: (err: HttpErrorResponse) => {
          if (err.status === 409) {
            this.popUpService.openPopUp('The email is already in use');
          } else {
            this.router.navigate(['/error']);
          }
        }
      });
    }
  }

  ngOnDestroy(): void {
    this.renderer.removeClass(document.body, 'search-results-page');
  }

}