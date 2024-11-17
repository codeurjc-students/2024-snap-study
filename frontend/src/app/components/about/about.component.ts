import { Component, Renderer2 } from "@angular/core";

@Component({
    selector: 'app-main',
    templateUrl: './about.component.html',
})
export class AboutComponent {

    constructor(private renderer: Renderer2) { }

    ngOnInit() {
        this.renderer.addClass(document.body, 'search-results-page');
    }

    ngOnDestroy(): void {
        this.renderer.removeClass(document.body, 'search-results-page');
    }
}