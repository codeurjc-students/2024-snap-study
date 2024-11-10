import { Component, Renderer2 } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { SearchService } from "../../services/search.service";
import { SubjectService } from "../../services/subject.service";
import { Subject } from "../../models/subject.model";
import { Degree } from "../../models/degree.model";

@Component({
    selector: 'app-search',
    templateUrl: './search.component.html',
})
export class SearchComponent {

    searchText: string = '';
    page: number = 0;
    size: number = 3;
    results: any = {
        degrees: [],
        subjects: [],
        documents: [],
        last: false
    };

    subject: any;
    degree: any;

    constructor(private subjectService: SubjectService, private searchService: SearchService, private route: ActivatedRoute, private router: Router, private renderer: Renderer2) { }

    ngOnInit() {
        this.renderer.addClass(document.body, 'search-results-page');
        this.route.params.subscribe(params => {
            this.searchText = params['text'];
        });

        this.executeSearch();
    }

    ngOnDestroy(): void {
        this.renderer.removeClass(document.body, 'search-results-page');
      }

    executeSearch() {
        this.searchService.search(this.searchText, this.page, this.size).subscribe(result => {
            this.results = result;
            console.log(this.results)
        });
    }

    loadMore() {
        if (!this.results.last) {
          this.page++;
          this.searchService.search(this.searchText, this.page, this.size).subscribe(result => {
            this.results.degrees = this.results.degrees.concat(result.degrees);
            this.results.subjects = this.results.subjects.concat(result.subjects);
            this.results.documents = this.results.documents.concat(result.documents);
            this.results.last = result.last;
          });
        }
      }

    navigateToSubject(id:number){
        this.subjectService.getDegreeBySubject(id).subscribe(
            (response: Degree) => {
              this.degree = response;
              this.router.navigate(['/degrees/' + this.degree.id + '/subjects/' + id]);
            },
            (error: any) => {
              this.router.navigate(['/error']);
            }
          );
    }

}
