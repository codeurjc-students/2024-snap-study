import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PopUpDialogComponent } from '../components/childs/popup-dialog.component';
import { BehaviorSubject } from 'rxjs';
import { PopUpImageComponent } from '../components/childs/popup-image.component';
import { PopUpDocumentComponent } from '../components/childs/popup-document.component';
import { Subject } from '../models/subject.model';
import { Degree } from '../models/degree.model';

@Injectable({
    providedIn: 'root'
})
export class PopUpService {

    private popupState = new BehaviorSubject<boolean>(false);
    popupState$ = this.popupState.asObservable();

    constructor(private dialog: MatDialog) { }

    openPopUp(message: string): void {
        this.popupState.next(true); // Cambia el estado a abierto
        this.dialog.open(PopUpDialogComponent, {
            width: '400px',
            position: { top: '50px', left: '50px' },
            panelClass: 'custom-dialog-container',
            data: message
        }).afterClosed().subscribe(() => {
            this.popupState.next(false); // Cambia el estado a cerrado
        });
    }

    openPopUpImage(){
        this.popupState.next(true);
        this.dialog.open(PopUpImageComponent, {
            width: '400px',
            position: { top: '50px', left: '50px' },
            panelClass: 'custom-dialog-container',
        }).afterClosed().subscribe(() => {
            this.popupState.next(false);
        });
    }

    openPopUpDocument(subject: Subject, degree: Degree) {
        this.popupState.next(true);
        this.dialog.open(PopUpDocumentComponent, {
            width: '400px',
            position: { top: '50px', left: '50px' },
            panelClass: 'custom-dialog-container',
            data: { subject, degree } // Pasamos los datos al modal
        }).afterClosed().subscribe(() => {
            this.popupState.next(false); 
        });
    }
}