import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

const BASE_URL = '/api/users/';

@Injectable({ providedIn: 'root' })
export class UserService {
    constructor(private http: HttpClient) { }

    editProfile(firstName: String, lastName: String, email: String, password: String) {
        return this.http.put(BASE_URL, { firstName, lastName, email, password })
    }

    setProfileImage(image: File) {
        const formData = new FormData();
        formData.append('file', image);
        return this.http.put('/api/users/image', formData, { withCredentials: true })
    }
}