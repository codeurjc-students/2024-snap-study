import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

const BASE_URL = '/api/documents/';

@Injectable({ providedIn: 'root' })
export class DocumentService {
    constructor(private http: HttpClient) { }

    getDocuments(id:number, index: number): Observable<any> {
        let params = new HttpParams();
        params = params.append('page', index.toString());
        params = params.append('size', '5');

        return this.http.get(BASE_URL + id, { params: params }) as Observable<any>;
    }

    // getSubject(id:number): Observable<any> {
    //     return this.http.get(BASE_URL + id) as Observable<any>;
    // }
}