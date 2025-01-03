import { Component, OnInit, Renderer2 } from '@angular/core';
import { Degree } from '../../models/degree.model';
import { DegreeService } from '../../services/degree.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-main',
    templateUrl: './admin-pannel.component.html',
})
export class AdminPannelComponent implements OnInit {

    public degrees: Degree[];

    public indexdegrees: number = 0;    //ajax
    public moredegrees: boolean = false;   //ajax

    constructor(private degreeService: DegreeService, public authService: AuthService, private router: Router, private renderer: Renderer2) {
        this.degrees = [];
    }

    ngOnInit() {
        this.renderer.addClass(document.body, 'search-results-page');
        this.authService.getCurrentUser().subscribe((loaded) => {
            if (loaded) {
                if (!this.authService.isLogged() || !this.authService.isAdmin()) {
                    this.router.navigate(['/error']); // Redirect to error if the user is not logged in or not an admin
                } else {
                    this.degreeService.getDegrees(this.indexdegrees).subscribe((response) => {
                        this.degrees = this.degrees.concat(response.content);
                        this.moredegrees = !response.last;
                        this.indexdegrees++; //next ajax button
                    });
                }
            } else {
                this.router.navigate(['/error']); // If the user could not be loaded correctly, redirect to error
            }
        });
    }

    getMoredegrees() {
        this.degreeService.getDegrees(this.indexdegrees).subscribe((response) => {
            this.degrees = this.degrees.concat(response.content);
            this.moredegrees = !response.last;
            this.indexdegrees++;
        });
    }

    showLoadMoreButton() {
        if (this.moredegrees) {
            return true;
        }
        return false;
    }

    deleteDegree(id: number) {
        this.degreeService.deleteDegree(id).subscribe({
            next: _ => {
                { this.reload(); }
            },
            error: (err: HttpErrorResponse) => {
                if (err.status === 204 || err.status === 200) {
                    this.reload();
                } else {
                    this.router.navigate(['/error']);
                }
            }
        });
    }

    reload() {
        this.indexdegrees = 0
        this.degreeService.getDegrees(0).subscribe((response) => {
            this.degrees = response.content;
            this.moredegrees = !response.last;
            this.indexdegrees++;
        });
    }

    ngOnDestroy(): void {
        this.renderer.removeClass(document.body, 'search-results-page');
    }

}