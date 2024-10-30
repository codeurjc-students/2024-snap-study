import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from '../../models/subject.model';
import { SubjectService } from '../../services/subject.service';
import { DocumentService } from '../../services/document.service';
import { Document } from '../../models/document.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-main',
  templateUrl: './document-list.component.html',
})
export class DocumentListComponent {

  public documents: Document[];
  public subject: any;
  private id: string;
  public indexdocuments: number = 0;    //ajax
  public moredocuments: boolean = false;   //ajax
  public selectedDocumentIds: number[] = [];

  constructor(public authService: AuthService, private documentService: DocumentService, private subjectService: SubjectService, private route: ActivatedRoute, private router: Router) {
    this.documents = [];
    this.id = "";
  }

  ngOnInit() {
    this.getDocuments();
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

  onCheckboxChange(event: any, id: number) {
    if (event.target.checked) {
      this.selectedDocumentIds.push(id);
    } else {
      this.selectedDocumentIds = this.selectedDocumentIds.filter(docId => docId !== id);
    }
  }

  //DESCARGARÁ O VOLCARÁ LOS DOCUMENTOS SEGÚN LOS IDS SELECCIONADOS
  getSelectedDocuments() {
    console.log(this.selectedDocumentIds); // Muestra los IDs seleccionados
  }

}