import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ObjetivoEspecifico, ObjetivoEspecificoRequest } from '../models/inventario.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ObjetivoEspecificoService {
  private apiUrl = `${environment.apiUrl}/objetivos-especificos`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<ObjetivoEspecifico[]> {
    return this.http.get<ObjetivoEspecifico[]>(this.apiUrl);
  }

  getById(id: number): Observable<ObjetivoEspecifico> {
    return this.http.get<ObjetivoEspecifico>(`${this.apiUrl}/${id}`);
  }

  create(request: ObjetivoEspecificoRequest): Observable<ObjetivoEspecifico> {
    return this.http.post<ObjetivoEspecifico>(this.apiUrl, request);
  }

  update(id: number, request: ObjetivoEspecificoRequest): Observable<ObjetivoEspecifico> {
    return this.http.put<ObjetivoEspecifico>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
