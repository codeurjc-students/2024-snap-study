import { Component, Renderer2 } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from '../../models/subject.model';
import { SubjectService } from '../../services/subject.service';
import { Degree } from '../../models/degree.model';
import { DegreeService } from '../../services/degree.service';
import { AuthService } from '../../services/auth.service';
import { timer } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-main',
  templateUrl: './admin-subjects.component.html',
})
export class AdminSubjectsComponent {

  public subjects: Subject[];
  public degree: any;
  private id: string;
  public indexsubjects: number = 0;    //ajax
  public moresubjects: boolean = false;   //ajax

  constructor(private renderer: Renderer2, private degreeService: DegreeService, private subjectService: SubjectService, private authService: AuthService, private router: Router, private route: ActivatedRoute) {
    this.subjects = [];
    this.id = "";
  }

  ngOnInit() {
    this.renderer.addClass(document.body, 'search-results-page');
    this.authService.getCurrentUser().subscribe(() => {
      if (!this.authService.isLogged() || !this.authService.isAdmin()) {
        this.router.navigate(['/error']);
      } else {
        this.getDegree();
      }
    });
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

  deleteSubject(id: number) {
    this.subjectService.deleteSubject(id, this.degree.id).subscribe({
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
    this.indexsubjects = 0
    this.subjectService.getSubjects(parseInt(this.id), 0).subscribe((response) => {
      this.subjects = response.content;
      this.moresubjects = !response.last;
      this.indexsubjects++;
    });
  }

  ngOnDestroy(): void {
    this.renderer.removeClass(document.body, 'search-results-page');
  }

}