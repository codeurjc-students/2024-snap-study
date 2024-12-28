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
    previewUrl: string | null = null;
    isPdf: boolean = false;
    isImage: boolean = false;

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

    // WILL DOWNLOAD OR EXPORT THE DOCUMENTS BASED ON THE SELECTED IDS
    getSelectedDocuments() {
        for (const id of this.selectedDocumentIds) {
            this.documentService.getDocument(id).subscribe(
                (doc: Document) => {
                    if (doc) {
                        // If the document is valid, proceed with the download
                        this.documentService.downloadDocument(id).subscribe(
                            (blob) => {
                                const url = window.URL.createObjectURL(blob); // Create a temporary URL
                                const a = document.createElement('a'); // Create a dynamic <a> element
                                a.href = url;
                                a.download = doc.name + doc.extension
                                a.style.display = 'none'; // Link not visible
                                document.body.appendChild(a); // Add the link to the DOM
                                a.click(); // Simulate a click to start the download
                                document.body.removeChild(a); // Remove the link from the DOM
                                window.URL.revokeObjectURL(url); // Release the temporary URL
                            },
                            (error) => {
                                console.error(`Error al descargar el documento con ID ${id}:`, error);
                                alert('It has not been possible to obtain the document ' + doc.name)
                            }
                        );
                    } else {
                        console.warn(`El documento con ID ${id} no es válido.`);
                        alert('It has not been possible to obtain the document')
                    }
                },
                (error) => {
                    console.error(`Error al obtener el documento con ID ${id}:`, error);
                    alert('It has not been possible to obtain the document')
                }
            );
        }
    }

    fetchDocument(id: number): void {
        this.documentService.getDocument(id).subscribe(
            (doc: Document) => {
                if (doc) {
                    this.documentService.downloadDocument(id).subscribe(
                        (blob) => {
                            const fileType = blob.type; // Get the file type
                            const extension = doc.extension

                            // Force the type if it arrives as 'application/octet-stream'
                            let inferredType = fileType;
                            if (fileType === 'application/octet-stream') {
                                if (extension === '.pdf') inferredType = 'application/pdf';
                                else if (['.png', '.jpg', '.jpeg', '.gif'].includes(extension)) {
                                    inferredType = `image/${extension.replace('.', '')}`;
                                }
                            }

                            // Create a Blob with the correct type if necessary
                            const correctedBlob = new Blob([blob], { type: inferredType });
                            const url = window.URL.createObjectURL(correctedBlob); // Create a temporary URL

                            // Check if it's a PDF or image
                            if (inferredType === 'application/pdf' || inferredType.startsWith('image/')) {
                                const newTab = window.open(url, '_blank'); // Open in a new tab
                                if (newTab) {
                                    newTab.focus();
                                } else {
                                    alert('The new tab could not be opened. Please enable popups');
                                }
                            } else {
                                alert('The document cannot be previewed by the browser');
                            }

                            // Release the temporary URL
                            window.URL.revokeObjectURL(url);
                        },
                        (error) => {
                            alert(`Error getting the content of the document ${doc.name}`);
                        }
                    );
                } else {
                    alert('The document cannot be previewed by the browser');
                }
            },
            (error) => {
                alert('Error getting the content of the document');
            }
        );
    }

    ngOnDestroy(): void {
        this.renderer.removeClass(document.body, 'search-results-page');
    }

}