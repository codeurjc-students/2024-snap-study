import { Component, Renderer2 } from '@angular/core';
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

  constructor(private renderer: Renderer2, public authService: AuthService, private documentService: DocumentService, private subjectService: SubjectService, private route: ActivatedRoute, private router: Router) {
    this.documents = [];
    this.id = "";
  }

  ngOnInit() {
    this.renderer.addClass(document.body, 'search-results-page');
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
    for (const id of this.selectedDocumentIds) {
      this.documentService.getDocument(id).subscribe(
        (doc : Document) => {
          if (doc) {
            // Si el documento es válido, proceder con la descarga
            this.documentService.downloadDocument(id).subscribe(
              (blob) => {
                const url = window.URL.createObjectURL(blob); // Crear URL temporal
                const a = document.createElement('a'); // Crear un elemento <a> dinámico
                a.href = url;
                a.download = doc.name + doc.extension
                a.style.display = 'none'; // Enlace no visible
                document.body.appendChild(a); // Agregar el enlace al DOM
                a.click(); // Simular clic para iniciar la descarga
                document.body.removeChild(a); // Eliminar el enlace del DOM
                window.URL.revokeObjectURL(url); // Liberar la URL temporal
              },
              (error) => {
                console.error(`Error al descargar el documento con ID ${id}:`, error);
              }
            );
          } else {
            console.warn(`El documento con ID ${id} no es válido.`);
          }
        },
        (error) => {
          console.error(`Error al obtener el documento con ID ${id}:`, error);
        }
      );
    }
  }

  ngOnDestroy(): void {
    this.renderer.removeClass(document.body, 'search-results-page');
  }

}