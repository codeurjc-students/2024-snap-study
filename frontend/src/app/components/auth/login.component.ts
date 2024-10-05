import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {

  public email: string = '';
  public password: string = '';
  private userLoadedSubscription: Subscription = new Subscription();

  constructor(private authService: AuthService, private router: Router){}

  sendCredentials(event: Event) {
    event.preventDefault();

    this.authService.login(this.email, this.password).subscribe({
      next: () => {
        this.userLoadedSubscription = this.authService.userLoaded().subscribe((loaded: boolean) => {
          if (loaded) {
            if (this.authService.isAdmin()) {
              this.router.navigate(['/admin']);
            } else {
              this.router.navigate(['/']);
            }
          }
        });
        this.authService.getCurrentUser();
      },
      error: (err: HttpErrorResponse) => {
        this.router.navigate(['/error']);
       },
     });
  }

}