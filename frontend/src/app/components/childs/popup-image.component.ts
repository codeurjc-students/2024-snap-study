import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PopUpService } from '../../services/popup.service';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
    selector: 'app-popup-image',
    templateUrl: './popup-image.component.html',
})

export class PopUpImageComponent implements OnInit {
    isOpen = false;
    public profilePicture: File

    constructor(private popUpService: PopUpService, private userService: UserService, private router: Router) {
        this.profilePicture = new File([], '')
    }

    ngOnInit(): void { 
        this.popUpService.popupState$.subscribe(state => {
            this.isOpen = state;
          });
    }

    openPopUp(message: string): void {
        this.popUpService.openPopUpImage();
    }

    onFileChange(event: Event) {
        event.preventDefault();
        const inputElement = event.target as HTMLInputElement;
        console.log(inputElement)
        if (inputElement.files && inputElement.files.length > 0) {
            this.profilePicture = inputElement.files[0];
        }
    }

    saveImage() {
        if (this.profilePicture.size > 0) {
            this.userService.setProfileImage(this.profilePicture).subscribe(
                () => {
                    this.router.navigate(['/profile']);
                }
            );
        }
    }
}
