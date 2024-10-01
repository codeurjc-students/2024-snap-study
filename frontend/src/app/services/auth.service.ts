import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable} from 'rxjs';
import { Router } from '@angular/router';

const BASE_URL = '/api/auth/';

@Injectable({ providedIn: 'root' })
export class AuthService {

    public logged: boolean = false;
    public admin: boolean = false;
    public student: boolean = false;

    constructor(private http: HttpClient, public router: Router) {}

    login(username: string, password: string): Observable<String> {
        return this.http.post<string>(BASE_URL + 'login', { username, password },{ withCredentials: true });
    }

    logout() {
        this.http
            .post<HttpResponse<any>>(BASE_URL + 'logout', { withCredentials: true })
            .subscribe(() => {
                this.logged = false;
                this.admin = false;
                this.student = false;
                this.router.navigate(['']);
            });
    }

    createUser(firstName: string, lastName: string, email: string, password: string) {
        return this.http.post<string>('/api/users/', {firstName, lastName, email, password});
    }
}
