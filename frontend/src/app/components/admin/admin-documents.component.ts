import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from '../../models/subject.model';
import { SubjectService } from '../../services/subject.service';
import { DocumentService } from '../../services/document.service';
import { Document } from '../../models/document.model';
import { AuthService } from '../../services/auth.service';
import { timer } from 'rxjs';
import { PopUpService } from '../../services/popup.service';
import { DegreeService } from '../../services/degree.service';
import { Degree } from '../../models/degree.model';

@Component({
  selector: 'app-main',
  templateUrl: './admin-documents.component.html',
  styleUrls: ['../../../styles.css', '../degrees-list/degree-list.component.css']
})
export class AdminDocumentsComponent {

  public documents: Document[];
  public subject: any;
  private id: string;
  private degree: any;
  private degreeId: string = '';
  public indexdocuments: number = 0;    //ajax
  public moredocuments: boolean = false;   //ajax
  public selectedDocumentIds: number[] = [];

  constructor(public authService: AuthService, private degreeService: DegreeService, private documentService: DocumentService, private popUpService: PopUpService, private subjectService: SubjectService, private route: ActivatedRoute, private router: Router) {
    this.documents = [];
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
      this.getDocuments();
      this.getDegree()
    });
  }

  getDocuments() {
    this.route.params.subscribe(params => {
      this.id = params['sid'];
    });
    this.subjectService.getSubject(parseInt(this.id)).subscribe(
      (response: Subject) => {
        this.subject = response;
        this.getMoredocuments();
      },
      (error: any) => {
        this.router.navigate(['/error']);
      }
    );
  }

  getMoredocuments() {
    this.documentService.getDocuments(parseInt(this.id), this.indexdocuments).subscribe((response) => {
      this.documents = this.documents.concat(response.content);
      this.moredocuments = !response.last;
      this.indexdocuments++;
    });
  }

  showLoadMoreButton() {
    if (this.moredocuments) {
      return true;
    }
    return false;
  }

  deleteDocument(id: number) { }

  getDegree() {
    this.route.params.subscribe(params => {
      this.degreeId = params['id'];
    });
    this.degreeService.getDegree(parseInt(this.degreeId)).subscribe(
      (response: Degree) => {
        this.degree = response;
      },
      (error: any) => {
        this.router.navigate(['/error']);
      }
    );
  }

  openModalAddDocument() {
    this.popUpService.openPopUpDocument(this.subject, this.degree);
  }

}