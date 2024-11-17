import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Degree } from '../../models/degree.model';
import { DegreeService } from '../../services/degree.service';

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',

})
export class MainComponent {
    title = 'SnapStudy';

    public searchText: string = '';
    public degrees: Degree[];

    public indexdegrees: number = 0;    //ajax
    public moredegrees: boolean = false;   //ajax

    constructor(private router: Router, private degreeService: DegreeService) {
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

    showLoadMoreButton() {
        if (this.moredegrees) {
            return true;
        }
        return false;
    }

    search() {
        this.router.navigate(['/search'], { queryParams: { query: this.searchText } });
    }
}