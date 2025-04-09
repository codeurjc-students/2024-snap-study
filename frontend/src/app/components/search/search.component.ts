import { Component, Renderer2 } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { SearchService } from "../../services/search.service";
import { SubjectService } from "../../services/subject.service";
import { Degree } from "../../models/degree.model";

@Component({
    selector: 'app-search',
    templateUrl: './search.component.html',
})
export class SearchComponent {

    searchText: string = '';
    page: number = 0;
    size: number = 3;
    results: any = []

    subject: any;
    degree: any;

    isLoading = false;

    constructor(private subjectService: SubjectService, private searchService: SearchService, private route: ActivatedRoute, private router: Router, private renderer: Renderer2) { }

    ngOnInit() {
        this.renderer.addClass(document.body, 'search-results-page');
        this.route.queryParams.subscribe(params => {
            this.searchText = params['query'];
        });

        this.executeSearch();
    }

    ngOnDestroy(): void {
        this.renderer.removeClass(document.body, 'search-results-page');
    }

    executeSearch() {
        this.isLoading = true;
        this.searchService.search(this.searchText, this.page, this.size).subscribe(result => {
            this.results = result;
            this.isLoading = false;
        }, error => {
            this.isLoading = false;
            // Puedes manejar errores aquí también
        });
    }

    loadMore() {

        
    }

    navigateToSubject(id: number) {
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
