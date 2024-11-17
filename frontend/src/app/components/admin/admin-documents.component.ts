import { Component, Renderer2 } from '@angular/core';
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
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-main',
    templateUrl: './admin-documents.component.html',
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

    constructor(private renderer: Renderer2, public authService: AuthService, private degreeService: DegreeService, private documentService: DocumentService, private popUpService: PopUpService, private subjectService: SubjectService, private route: ActivatedRoute, private router: Router) {
        this.documents = [];
        this.id = "";
    }

    ngOnInit() {
        this.renderer.addClass(document.body, 'search-results-page');
        this.authService.getCurrentUser().subscribe((loaded) => {
            if (loaded) {
                if (!this.authService.isLogged() || !this.authService.isAdmin()) {
                    this.router.navigate(['/error']); // Redirige a error si no está logueado o no es admin
                } else {
                    this.getDocuments();
                    this.getDegree();
                    this.popUpService.documentSaved$.subscribe(() => {
                        this.reload(); // Recargar después de que se haya guardado un documento
                    });
                }
            } else {
                // Si no se pudo cargar el usuario correctamente, redirigir a error
                this.router.navigate(['/error']);
            }
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

    reload() {
        this.indexdocuments = 0
        this.documentService.getDocuments(parseInt(this.id), 0).subscribe((response) => {
            this.documents = response.content;
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

    deleteDocument(id: number) {
        this.documentService.deleteDocument(id, this.degree.id, this.subject.id).subscribe({
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

    ngOnDestroy(): void {
        this.renderer.removeClass(document.body, 'search-results-page');
    }

    fetchDocument(id: number): void {
        this.documentService.getDocument(id).subscribe(
            (doc: Document) => {
                if (doc) {
                    this.documentService.downloadDocument(id).subscribe(
                        (blob) => {
                            const fileType = blob.type; // Obtener el tipo de archivo
                            const extension = doc.extension

                            // Forzar el tipo si llega como 'application/octet-stream'
                            let inferredType = fileType;
                            if (fileType === 'application/octet-stream') {
                                if (extension === '.pdf') inferredType = 'application/pdf';
                                else if (['.png', '.jpg', '.jpeg', '.gif'].includes(extension)) {
                                    inferredType = `image/${extension.replace('.', '')}`;
                                }
                            }

                            // Crear un Blob con el tipo correcto si es necesario
                            const correctedBlob = new Blob([blob], { type: inferredType });
                            const url = window.URL.createObjectURL(correctedBlob); // Crear URL temporal

                            // Verificar si es PDF o imagen
                            if (inferredType === 'application/pdf' || inferredType.startsWith('image/')) {
                                const newTab = window.open(url, '_blank'); // Abrir en nueva pestaña
                                if (newTab) {
                                    newTab.focus();
                                } else {
                                    alert('No se pudo abrir la nueva pestaña. Por favor, habilite los popups.');
                                }
                            } else {
                                alert('El documento no puede ser previsualizado por el navegador');
                            }

                            // Liberar la URL temporal
                            window.URL.revokeObjectURL(url);
                        },
                        (error) => {
                            alert(`Error al obtener el contenido del documento ${doc.name}`);
                        }
                    );
                } else {
                    alert('El documento no puede ser previsualizado por el navegador');
                }
            },
            (error) => {
                alert('Error al obtener el contenido del documento');
            }
        );
    }

}