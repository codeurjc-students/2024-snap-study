import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

const BASE_URL = '/api/degrees/';

@Injectable({ providedIn: 'root' })
export class DegreeService {
    constructor(private http: HttpClient) { }

    getDegrees(index: number): Observable<any> {
        let params = new HttpParams();
        params = params.append('page', index.toString());
        params = params.append('size', '5');

        return this.http.get(BASE_URL, { params: params }) as Observable<any>;
    }
}