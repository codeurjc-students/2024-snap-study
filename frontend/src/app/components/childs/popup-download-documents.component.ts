import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PopUpService } from '../../services/popup.service';

@Component({
    selector: 'app-popup-download-drive',
    templateUrl: './popup-download-documents.component.html'
})
export class PopUpDownloadDriveComponent {

    isOpen = false;

    constructor(private popUpService: PopUpService, private dialogRef: MatDialogRef<PopUpDownloadDriveComponent>) {}

    ngOnInit(): void {
        this.popUpService.popupState$.subscribe(state => {
            this.isOpen = state;
        });
    }

    
    selectOption(option: number) {
        this.dialogRef.close(option); // Cierra el popup y devuelve la opción seleccionada
    }
}