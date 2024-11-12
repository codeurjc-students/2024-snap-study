import { Component, Renderer2 } from '@angular/core';
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

  constructor(private degreeService: DegreeService, private renderer: Renderer2) {
    this.degrees = [];
  }

  ngOnInit() {
    this.renderer.addClass(document.body, 'search-results-page');
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

  showLoadMoreButton() {
    if (this.moredegrees) {
      return true;
    }
    return false;
  }

  ngOnDestroy(): void {
    this.renderer.removeClass(document.body, 'search-results-page');
  }

}