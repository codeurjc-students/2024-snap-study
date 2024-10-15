import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { User } from '../models/user.model';

const BASE_URL = '/api/auth/';

@Injectable({ providedIn: 'root' })
export class AuthService {

    private currentUser: User = {} as User;
    public logged: boolean = false;
    public admin: boolean = false;
    public student: boolean = false;
    private userLoadedSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

    constructor(private http: HttpClient, public router: Router) { }

    login(username: string, password: string): Observable<String> {
        return this.http.post<string>(BASE_URL + 'login', { username, password }, { withCredentials: true });
    }

    logout() {
        this.http.post<HttpResponse<any>>(BASE_URL + 'logout', { withCredentials: true }).subscribe(() => {
            this.logged = false;
            this.admin = false;
            this.student = false;
            this.router.navigate(['']);
        });
    }

    createUser(firstName: string, lastName: string, email: string, password: string) {
        return this.http.post<string>('/api/users/', { firstName, lastName, email, password });
    }

    userLoaded(): Observable<boolean> {
        return this.userLoadedSubject.asObservable();
    }

    isAdmin(): boolean {
        return this.admin;
    }

    getCurrentUser() {
        this.http.get<User>('/api/users/me', { withCredentials: true }).subscribe((response: User) => {
            this.currentUser = response;
            this.logged = true;
            this.admin = this.currentUser.roles.includes('ADMIN');
            this.student = this.currentUser.roles.includes('STUDENT');
            this.userLoadedSubject.next(true);
        });
    }

    isLogged(): boolean {
        return this.logged;
    }

    getUser(): User {
        return this.currentUser;
    }
}
