import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

const BASE_URL = '/api/users/';

@Injectable({ providedIn: 'root' })
export class UserService {
    constructor(private http: HttpClient) { }

    /*getUserDegrees(index: number): Observable<any> {
        let params = new HttpParams();
        params = params.append('page', index.toString());
        params = params.append('size', '5');

        return this.http.get(BASE_URL, { params: params }) as Observable<any>;
    }*/
}