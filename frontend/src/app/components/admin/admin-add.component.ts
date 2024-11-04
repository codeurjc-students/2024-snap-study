import { Location } from "@angular/common";
import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { DegreeService } from "../../services/degree.service";
import { HttpErrorResponse } from "@angular/common/http";
import { PopUpService } from "../../services/popup.service";
import { SubjectService } from "../../services/subject.service";
import { AuthService } from "../../services/auth.service";

@Component({
    selector: 'app-main',
    templateUrl: './admin-add.component.html',
})
export class AdminAddComponent {
    public isDegree: boolean = false;
    public name: string = '';
    public isDegreeParam: number = -1;
    private degree: any;

    constructor(private authService: AuthService, private subjectService: SubjectService, private popUpService: PopUpService, private degreeService: DegreeService, private route: ActivatedRoute, private router: Router, private location: Location) { }

    ngOnInit() {
        this.authService.userLoaded().subscribe((loaded) => {
            if (!this.authService.isLogged() || !this.authService.isAdmin()) {
                this.router.navigate(['/error']); // Redirige a error si no es admin
            }
        });
        this.route.params.subscribe(params => {
            this.isDegreeParam = params['isDegree'];
            this.degree = params['degreeId']
            if (this.isDegreeParam == 1) {
                this.isDegree = true
            }
        });
    }

    goBack() {
        this.location.back();  // Navega hacia la ruta anterior en el historial
    }

    createDegree(event: Event) {
        event.preventDefault();
        this.degreeService.saveDegree(this.name).subscribe(
            {
                next: _ => {
                    this.location.back()
                },
                error: (err: HttpErrorResponse) => {
                    if (err.status === 409) {
                        this.popUpService.openPopUp('This degree is already exists');
                    } else {
                        this.router.navigate(['/error']);
                    }
                }
            });
    }

    createSubject(event: Event) {
        event.preventDefault();
        if(this.degree != null){
            this.subjectService.saveSubject(this.name, this.degree).subscribe(
                {
                    next: _ => {
                        this.location.back()
                    },
                    error: (err: HttpErrorResponse) => {
                        if (err.status === 409) {
                            this.popUpService.openPopUp('This subject is already exists');
                        } else {
                            this.router.navigate(['/error']);
                        }
                    }
                });
        } else {
            this.router.navigate(['/error']);
        }
    }
}