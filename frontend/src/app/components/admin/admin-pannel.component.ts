import { Component, OnInit } from '@angular/core';
import { Degree } from '../../models/degree.model';
import { DegreeService } from '../../services/degree.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { timer } from 'rxjs';

@Component({
    selector: 'app-main',
    templateUrl: './admin-pannel.component.html',
})
export class AdminPannelComponent implements OnInit {

    public degrees: Degree[];

    public indexdegrees: number = 0;    //ajax
    public moredegrees: boolean = false;   //ajax

    constructor(private degreeService: DegreeService, public authService: AuthService, private router: Router) {
        this.degrees = [];
    }

    ngOnInit() {
        this.authService.getCurrentUser()
        timer(1000).subscribe(() => {
            this.authService.userLoaded().subscribe((loaded) => {
                if (!this.authService.isLogged() || !this.authService.isAdmin()) {
                    this.router.navigate(['/error']); // Redirige a error si no es admin
                }
            });
            this.degreeService.getDegrees(this.indexdegrees).subscribe((response) => {
                this.degrees = this.degrees.concat(response.content);
                this.moredegrees = !response.last;
                this.indexdegrees++; //next ajax buttom
            });
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

    deleteDegree(id:number){}

}