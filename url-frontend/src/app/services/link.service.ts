import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Link } from '../models/link.model';
import { environment } from '../../environments/environment';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LinkService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  shorten(url: string, customAlias?: string, expiresAt?: string): Observable<Link> {
    return this.http.post<Link>(`${this.baseUrl}/shorten`, { url, customAlias, expiresAt })
      .pipe(
        map(link => ({
          ...link,
          createdAt: new Date(link.createdAt),
          expiresAt: link.expiresAt ? new Date(link.expiresAt) : undefined
        }))
      );
  }

  getLink(code: string): Observable<Link> {
    return this.http.get<Link>(`${this.baseUrl}/api/links/${code}`);
  }

  updateLink(code: string, data: any): Observable<Link> {
    return this.http.put<Link>(`${this.baseUrl}/api/links/${code}`, data);
  }

  deleteLink(code: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/links/${code}`);
  }
}