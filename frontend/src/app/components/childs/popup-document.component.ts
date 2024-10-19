import { Component, Inject, OnInit } from '@angular/core';
import { PopUpService } from '../../services/popup.service';
import { Router } from '@angular/router';
import { DocumentService } from '../../services/document.service';
import { Degree } from '../../models/degree.model';
import { Subject } from '../../models/subject.model';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
    selector: 'app-popup-document',
    templateUrl: './popup-document.component.html',
})

export class PopUpDocumentComponent implements OnInit {
    isOpen = false;
    public document: File;
    public subject: Subject; 
    public degree: Degree;

    constructor(
        private popUpService: PopUpService, 
        private documentService: DocumentService, 
        private router: Router, 
        @Inject(MAT_DIALOG_DATA) public data: { subject: Subject, degree: Degree } // Recibir datos
    ) {
        this.document = new File([], '');

        this.subject = data.subject;
        this.degree = data.degree;
    }

    ngOnInit(): void {
        this.popUpService.popupState$.subscribe(state => {
            this.isOpen = state;
        });
    }

    onFileChange(event: Event) {
        event.preventDefault();
        const inputElement = event.target as HTMLInputElement;
        if (inputElement.files && inputElement.files.length > 0) {
            this.document = inputElement.files[0];
        }
    }

    saveDocument() {
        if (this.document.size > 0) {
            this.documentService.saveDocument(this.document, this.degree.id, this.subject.id).subscribe(
                () => {
                    window.location.reload();
                },
                (error) => {
                    this.router.navigate(['/error']);
                }
            );
        }
    }
}
