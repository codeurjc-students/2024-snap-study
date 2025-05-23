import { Component, Renderer2 } from "@angular/core";
import { User } from "../../models/user.model";
import { AuthService } from "../../services/auth.service";
import { Router } from "@angular/router";
import { UserService } from "../../services/user.service";
import { HttpErrorResponse } from "@angular/common/http";
import { PopUpService } from "../../services/popup.service";

@Component({
    selector: 'app-main',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.css', '../../../styles.css']
})
export class ProfileComponent {

    public firstName: String = '';
    public lastName: String = '';
    public email: String = '';
    public password: String = '';
    public password2: String = '';
    private user: User = {} as User;

    constructor(private renderer: Renderer2, private authService: AuthService, private router: Router, private userService: UserService, private popUpService: PopUpService) {
    }

    ngOnInit() {
        this.renderer.addClass(document.body, 'search-results-page');
        this.authService.getCurrentUser().subscribe((loaded: boolean) => {
            if (loaded) {
                this.loadUserData();
            } else {
                this.router.navigate(['/error']);
            }
        });
    }

    loadUserData() {
        this.user = this.authService.getUser();
        this.firstName = this.user.firstName
        this.lastName = this.user.lastName
        this.email = this.user.email
    }

    editProfile() {
        if (this.firstName === "" || this.lastName === "") {
            this.popUpService.openPopUp('Some field is incomplete');
        } else if (this.password !== this.password2 || this.password === "") {
            this.popUpService.openPopUp('Passwords do not match');
        } else {
            this.userService.editProfile(this.firstName, this.lastName, this.email, this.password).subscribe({
                next: (_: any) => {
                    // Wait for getCurrentUser to complete before redirecting
                    this.authService.getCurrentUser().subscribe((loaded: boolean) => {
                        if (loaded) {
                            this.router.navigate(['/profile']);
                        } else {
                            this.router.navigate(['/error']);
                        }
                    });
                },
                error: (err: HttpErrorResponse) => {
                    if (err.status === 400) {
                        this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
                            this.router.navigate(['/profile']);
                        });
                    } else {
                        this.router.navigate(['/error']);
                    }
                }
            });
        }
    }

    editImage() {
        this.popUpService.openPopUpImage();
    }

    logout() {
        this.authService.logout();
    }

    ngOnDestroy(): void {
        this.renderer.removeClass(document.body, 'search-results-page');
    }
}