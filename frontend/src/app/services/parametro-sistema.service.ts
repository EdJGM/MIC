import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ParametroSistema, ParametroSistemaRequest } from '../models/configuracion.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ParametroSistemaService {
  private apiUrl = `${environment.apiUrl}/parametros`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<ParametroSistema[]> {
    return this.http.get<ParametroSistema[]>(this.apiUrl);
  }

  getByClave(clave: string): Observable<ParametroSistema> {
    return this.http.get<ParametroSistema>(`${this.apiUrl}/${clave}`);
  }

  update(clave: string, request: ParametroSistemaRequest): Observable<ParametroSistema> {
    return this.http.put<ParametroSistema>(`${this.apiUrl}/${clave}`, request);
  }
}
