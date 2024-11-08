import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
})
export class HeaderComponent {

  public isLogged: boolean = false;
  public isAdmin: boolean = false;

  constructor(public authService: AuthService) { }

  ngOnInit(): void {
    this.authService.getCurrentUser().subscribe((loaded) => {
      if (loaded) {
        this.isLogged = this.authService.isLogged();
        this.isAdmin = this.authService.isAdmin();
      } else {
        console.error('Error: User data could not be loaded.');
      }
    });
  }
}