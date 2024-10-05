import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',

})
export class HeaderComponent {

  public isLogged: boolean = false;
  public isAdmin: boolean = false;

  constructor(public authService: AuthService) {
    this.authService.getCurrentUser();
  }

  ngOnInit(): void {
    this.isLogged = this.authService.isLogged();
    this.isAdmin = this.authService.isAdmin();
  }

  logout() {
    this.authService.logout();
  }
}