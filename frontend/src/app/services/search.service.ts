import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { SearchResult } from "../models/searchResult.model";

const BASE_URL = '/api/search'

@Injectable({
    providedIn: 'root'
})
export class SearchService {

    constructor(private http: HttpClient) { }

    search(query: string, page: number, size: number): Observable<SearchResult[]> {
        const params = new HttpParams()
            .set('query', query)
            .set('page', page.toString())
            .set('size', size.toString());

        return this.http.get<SearchResult[]>(BASE_URL, { params });
    }
}