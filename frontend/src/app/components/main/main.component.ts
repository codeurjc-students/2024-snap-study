import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',

})
export class MainComponent {
  title = 'SnapStudy';

  public searchText: string = '';

  constructor(private router: Router){}

  search(){
    this.router.navigate(['/search', this.searchText]);
  }
}