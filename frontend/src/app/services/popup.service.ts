import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PopUpDialogComponent } from '../components/childs/popup-dialog.component';
import { BehaviorSubject, Subject as RxSubject } from 'rxjs';
import { PopUpImageComponent } from '../components/childs/popup-image.component';
import { PopUpDocumentComponent } from '../components/childs/popup-document.component';
import { Subject } from '../models/subject.model';
import { Degree } from '../models/degree.model';
import { PopUpDownloadDriveComponent } from '../components/childs/popup-download-documents.component';

@Injectable({
    providedIn: 'root'
})
export class PopUpService {

    private popupState = new BehaviorSubject<boolean>(false);
    popupState$ = this.popupState.asObservable();

    private documentSavedSource = new RxSubject<void>();
    documentSaved$ = this.documentSavedSource.asObservable();

    constructor(private dialog: MatDialog) { }

    openPopUp(message: string): void {
        document.body.style.overflow = 'hidden';
        this.popupState.next(true); // Change the state to open
        this.dialog.open(PopUpDialogComponent, {
            width: '400px',
            position: { top: '50px', left: '50px' },
            panelClass: 'custom-dialog-container',
            data: message
        }).afterClosed().subscribe(() => {
            this.popupState.next(false); // Change the state to closed
            document.body.style.overflow = '';
        });
    }

    openPopUpImage() {
        document.body.style.overflow = 'hidden';
        this.popupState.next(true);
        this.dialog.open(PopUpImageComponent, {
            width: '400px',
            position: { top: '50px', left: '50px' },
            panelClass: 'custom-dialog-container',
        }).afterClosed().subscribe(() => {
            this.popupState.next(false);
            document.body.style.overflow = '';
        });
    }

    openPopUpDocument(subject: Subject, degree: Degree) {
        document.body.style.overflow = 'hidden';
        document.documentElement.style.overflow = 'hidden';
        this.popupState.next(true);
        this.dialog.open(PopUpDocumentComponent, {
            width: '400px',
            position: { top: '50px', left: '50px' },
            panelClass: 'custom-dialog-container',
            data: { subject, degree } // Pass the data to the modal
        }).afterClosed().subscribe(() => {
            this.popupState.next(false);
            document.body.style.overflow = '';
            document.documentElement.style.overflow = '';
        });
    }

    notifyDocumentSaved() {
        this.documentSavedSource.next();
    }

    openPopUpDownloadDrive(): Promise<number> {
        document.body.style.overflow = 'hidden';
        this.popupState.next(true);
        return this.dialog.open(PopUpDownloadDriveComponent, {
            width: '400px',
            position: { top: '50px', left: '50px' },
            panelClass: 'custom-dialog-container',
        }).afterClosed().toPromise().then((result) => {
            this.popupState.next(false);
            document.body.style.overflow = '';
            return result ?? -1; // If the user closes without selecting anything, return -1
        });
    }
}