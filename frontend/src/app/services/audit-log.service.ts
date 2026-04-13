import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuditLog } from '../models/configuracion.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuditLogService {
  private apiUrl = `${environment.apiUrl}/audit`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<AuditLog[]> {
    return this.http.get<AuditLog[]>(this.apiUrl);
  }

  filtrar(modulo?: string, desde?: string, hasta?: string): Observable<AuditLog[]> {
    let params = new HttpParams();
    if (modulo) params = params.set('modulo', modulo);
    if (desde)  params = params.set('desde', desde);
    if (hasta)  params = params.set('hasta', hasta);
    return this.http.get<AuditLog[]>(this.apiUrl, { params });
  }
}
