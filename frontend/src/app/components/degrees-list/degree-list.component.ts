import { Component } from '@angular/core';
import { User } from '../../models/user.model';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Degree } from '../../models/degree.model';
import { DegreeService } from '../../services/degree.service';

@Component({
  selector: 'app-main',
  templateUrl: './degree-list.component.html',
  styleUrl: './degree-list.component.css'
})
export class DegreeListComponent {

  public user!: User;
  public degrees: Degree[];
  public isStudent!: boolean;

  public indexdegrees: number = 0;    //ajax
  public moredegrees: boolean = false;   //ajax

  constructor(public authService: AuthService, private degreeService: DegreeService, private router: Router, private route: ActivatedRoute) {
    this.degrees = [];
  }

  ngOnInit() {
    this.degreeService.getDegrees(this.indexdegrees).subscribe((response) => {
      this.degrees = this.degrees.concat(response.content);
      this.moredegrees = !response.last;
      this.indexdegrees++; //next ajax buttom
    });
  }

  getMoredegrees() {
    this.degreeService.getDegrees(this.indexdegrees).subscribe((response) => {
        this.degrees = this.degrees.concat(response.content);
        this.moredegrees = !response.last;
        this.indexdegrees++;
      });
  }

}