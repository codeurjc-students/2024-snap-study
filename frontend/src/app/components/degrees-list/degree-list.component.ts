import { Component } from '@angular/core';
import { Degree } from '../../models/degree.model';
import { DegreeService } from '../../services/degree.service';

@Component({
  selector: 'app-degree-list',
  templateUrl: './degree-list.component.html',
})
export class DegreeListComponent {

  public degrees: Degree[];

  public indexdegrees: number = 0;    //ajax
  public moredegrees: boolean = false;   //ajax

  constructor(private degreeService: DegreeService) {
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

  showLoadMoreButton(){
    if (this.moredegrees){
      return true;
    }
    return false;
  }

}