import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Document } from '../models/document.model';

const BASE_URL = '/api/documents/';

@Injectable({ providedIn: 'root' })
export class DocumentService {
    constructor(private http: HttpClient) { }

    getDocuments(id: number, index: number): Observable<any> {
        let params = new HttpParams();
        params = params.append('page', index.toString());
        params = params.append('size', '5');

        return this.http.get(BASE_URL + id, { params: params }) as Observable<any>;
    }

    saveDocument(document: File, degree: number, subject: number) {
        const formData = new FormData();
        formData.append('file', document);
        return this.http.post(BASE_URL + degree + "/" + subject, formData);
    }

    deleteDocument(id: number, degreeId: number, subjectId: number): Observable<any> {
        return this.http.delete(BASE_URL + degreeId + "/" + subjectId + "/" + id) as Observable<any>;
    }

    downloadDocument(id: number) {
        return this.http.get(BASE_URL + "download/" + id, { responseType: 'blob' });
    }

    getDocument(id: number): Observable<Document> {
        return this.http.get<Document>(BASE_URL, { params: { id: id.toString() } });
    }
}