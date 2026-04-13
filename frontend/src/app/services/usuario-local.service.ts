import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UsuarioLocal, UsuarioLocalRequest, RolLocal } from '../models/configuracion.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsuarioLocalService {
  private apiUrl = `${environment.apiUrl}/usuarios-locales`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<UsuarioLocal[]> {
    return this.http.get<UsuarioLocal[]>(this.apiUrl);
  }

  getPorActivo(activo: boolean): Observable<UsuarioLocal[]> {
    const params = new HttpParams().set('activo', activo);
    return this.http.get<UsuarioLocal[]>(this.apiUrl, { params });
  }

  getPorRol(rol: RolLocal): Observable<UsuarioLocal[]> {
    const params = new HttpParams().set('rol', rol);
    return this.http.get<UsuarioLocal[]>(this.apiUrl, { params });
  }

  getById(id: number): Observable<UsuarioLocal> {
    return this.http.get<UsuarioLocal>(`${this.apiUrl}/${id}`);
  }

  create(request: UsuarioLocalRequest): Observable<UsuarioLocal> {
    return this.http.post<UsuarioLocal>(this.apiUrl, request);
  }

  update(id: number, request: UsuarioLocalRequest): Observable<UsuarioLocal> {
    return this.http.put<UsuarioLocal>(`${this.apiUrl}/${id}`, request);
  }

  desactivar(id: number): Observable<UsuarioLocal> {
    return this.http.patch<UsuarioLocal>(`${this.apiUrl}/${id}/desactivar`, {});
  }

  activar(id: number): Observable<UsuarioLocal> {
    return this.http.patch<UsuarioLocal>(`${this.apiUrl}/${id}/activar`, {});
  }
}
