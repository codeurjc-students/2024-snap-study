import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from '../../models/subject.model';
import { SubjectService } from '../../services/subject.service';
import { Degree } from '../../models/degree.model';
import { DegreeService } from '../../services/degree.service';
import { AuthService } from '../../services/auth.service';
import { timer } from 'rxjs';

@Component({
  selector: 'app-main',
  templateUrl: './admin-subjects.component.html',
  styleUrls: ['../../../styles.css', '../degrees-list/degree-list.component.css']
})
export class AdminSubjectsComponent {

  public subjects: Subject[];
  public degree: any;
  private id: string;
  public indexsubjects: number = 0;    //ajax
  public moresubjects: boolean = false;   //ajax

  constructor(private degreeService: DegreeService, private subjectService: SubjectService, private authService: AuthService, private router: Router, private route: ActivatedRoute) {
    this.subjects = [];
    this.id = "";
  }

  ngOnInit() {
    this.authService.getCurrentUser()
    timer(1000).subscribe(() => {
        this.authService.userLoaded().subscribe((loaded) => {
            if (!this.authService.isLogged() || !this.authService.isAdmin()) {
                this.router.navigate(['/error']); // Redirige a error si no es admin
            }
        });
        this.getDegree();
    });
    
  }

  getDegree(){
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

  showLoadMoreButton(){
    if (this.moresubjects){
      return true;
    }
    return false;
  }

  deleteSubject(is:number){}

}