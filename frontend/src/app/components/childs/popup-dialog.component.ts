import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PopUpService } from '../../services/popup.service';

@Component({
  selector: 'app-popup-dialog',
  templateUrl: './popup-dialog.component.html',
})

export class PopUpDialogComponent implements OnInit {
  isOpen = false; 
  message: string = '';

  constructor(private popUpService: PopUpService, @Inject(MAT_DIALOG_DATA) public data: any ) { }

  ngOnInit(): void {
    this.popUpService.popupState$.subscribe(state => {
      this.isOpen = state;
    });

    this.message = this.data;
  }

  openPopUp(message: string): void {
    this.popUpService.openPopUp(message);
  }
}
