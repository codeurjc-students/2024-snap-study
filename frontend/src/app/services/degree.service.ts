import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Degree } from "../models/degree.model";

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

    getDegree(id: number): Observable<Degree> {
        return this.http.get(BASE_URL + id) as Observable<Degree>
    }

    saveDegree(name: string): Observable<Degree> {
        return this.http.post(BASE_URL, name) as Observable<Degree>
    }

    deleteDegree(id: number): Observable<any> {
        return this.http.delete(BASE_URL + id) as Observable<any>;
    }

    getDegreeTypes(): Observable<string[]> {
        return this.http.get(BASE_URL + "types") as Observable<string[]>
    }
}