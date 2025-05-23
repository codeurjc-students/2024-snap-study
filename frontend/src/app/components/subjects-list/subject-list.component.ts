import { Component, Renderer2 } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from '../../models/subject.model';
import { SubjectService } from '../../services/subject.service';
import { Degree } from '../../models/degree.model';
import { DegreeService } from '../../services/degree.service';

@Component({
    selector: 'app-main',
    templateUrl: './subject-list.component.html',
})
export class SubjectListComponent {

    public subjects: Subject[];
    public degree: any;
    private id: string;
    public indexsubjects: number = 0;    //ajax
    public moresubjects: boolean = false;   //ajax

    constructor(private renderer: Renderer2, private degreeService: DegreeService, private subjectService: SubjectService, private route: ActivatedRoute, private router: Router) {
        this.subjects = [];
        this.id = "";
    }

    ngOnInit() {
        this.renderer.addClass(document.body, 'search-results-page');
        this.getDegree();
    }

    getDegree() {
        this.route.params.subscribe(params => {
            this.id = params['id'];
        });
        this.degreeService.getDegree(parseInt(this.id)).subscribe(
            (response: Degree) => {
                this.degree = response;
                this.getMoresubjects();
            },
            (error: any) => {
                this.router.navigate(['/error']);
            }
        );
    }

    getMoresubjects() {
        this.subjectService.getSubjects(parseInt(this.id), this.indexsubjects).subscribe((response) => {
            this.subjects = this.subjects.concat(response.content);
            this.moresubjects = !response.last;
            this.indexsubjects++;
        });
    }

    showLoadMoreButton() {
        if (this.moresubjects) {
            return true;
        }
        return false;
    }

    ngOnDestroy(): void {
        this.renderer.removeClass(document.body, 'search-results-page');
    }

}