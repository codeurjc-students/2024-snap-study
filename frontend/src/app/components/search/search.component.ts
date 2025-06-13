import { Component, Renderer2 } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { SearchService } from "../../services/search.service";
import { SubjectService } from "../../services/subject.service";
import { Degree } from "../../models/degree.model";
import { AuthService } from "../../services/auth.service";
import { PopUpService } from "../../services/popup.service";
import { DocumentService } from "../../services/document.service";
import { Document } from '../../models/document.model';
import { HttpErrorResponse } from "@angular/common/http";

@Component({
    selector: 'app-search',
    templateUrl: './search.component.html',
})
export class SearchComponent {

    searchText: string = '';
    page: number = 0;
    size: number = 3;
    results: any = []

    subject: any;
    degree: any;

    isLoading = false;

    public selectedDocumentIds: number[] = [];

    public indexdocuments: number = 0;    //ajax
    public moredocuments: boolean = false;   //ajax
    previewUrl: string | null = null;
    isPdf: boolean = false;
    isImage: boolean = false;

    constructor(private popUpService: PopUpService, private documentService: DocumentService, private searchService: SearchService, private route: ActivatedRoute, private router: Router, private renderer: Renderer2, public authService: AuthService) { }

    ngOnInit() {
        this.renderer.addClass(document.body, 'search-results-page');
        this.route.queryParams.subscribe(params => {
            this.searchText = params['query'];
        });

        this.executeSearch();
    }

    ngOnDestroy(): void {
        this.renderer.removeClass(document.body, 'search-results-page');
    }

    executeSearch() {
        this.isLoading = true;
        this.searchService.search(this.searchText, this.page, this.size).subscribe(result => {
            this.results = result;
            this.isLoading = false;
            console.log(this.results)
        }, error => {
            this.isLoading = false;
            // Puedes manejar errores aquí también
        });
    }

    onCheckboxChange(event: any, id: number) {
        if (event.target.checked) {
            this.selectedDocumentIds.push(id);
        } else {
            this.selectedDocumentIds = this.selectedDocumentIds.filter(docId => docId !== id);
        }
    }

    downloadSelected() {
        if(this.selectedDocumentIds.length > 0){
            this.popUpService.openPopUpDownloadDrive().then(option => {
                if (option === 0) {
                    this.getSelectedDocuments()
                } else if (option === 1) {
                    this.downloadInGoogleDrive()
                } else {
                    console.log('Exit');
                }
            });
        }
    }

    downloadInGoogleDrive() {
        for (const id of this.selectedDocumentIds) {
            console.log(id)
            this.documentService.getDocument(id).subscribe(
                (doc: Document) => {
                    if (doc) {
                        console.log(doc)
                        // If the document is valid, proceed with the download
                        this.documentService.downloadDocumentGD(id).subscribe({
                            next: _ => {
                                { console.log('DONE') }
                            },
                            error: (err: HttpErrorResponse) => {
                                console.error(`Error downloading the document with ID ${id}:`, err);
                                alert('It has not been possible to obtain the document ' + doc.name)
                            }
                        });
                    } else {
                        alert('It has not been possible to obtain the document')
                    }
                },
                (error) => {
                    alert('It has not been possible to obtain the document')
                }
            );
        }
    }

    // WILL DOWNLOAD OR EXPORT THE DOCUMENTS BASED ON THE SELECTED IDS
    getSelectedDocuments() {
        for (const id of this.selectedDocumentIds) {
            console.log(id)
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
                                alert('It has not been possible to obtain the document ' + doc.name)
                            }
                        );
                    } else {
                        alert('It has not been possible to obtain the document')
                    }
                },
                (error) => {
                    alert('It has not been possible to obtain the document')
                }
            );
        }
    }

}
