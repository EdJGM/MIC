import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Subproceso,
  SubprocesoRequest,
  EstadoDocumentacion
} from '../models/inventario.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SubprocesoService {
  private apiUrl = `${environment.apiUrl}/subprocesos`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<Subproceso[]> {
    return this.http.get<Subproceso[]>(this.apiUrl);
  }

  getById(id: number): Observable<Subproceso> {
    return this.http.get<Subproceso>(`${this.apiUrl}/${id}`);
  }

  getByProceso(procesoId: number): Observable<Subproceso[]> {
    return this.http.get<Subproceso[]>(`${this.apiUrl}/proceso/${procesoId}`);
  }

  create(request: SubprocesoRequest): Observable<Subproceso> {
    return this.http.post<Subproceso>(this.apiUrl, request);
  }

  update(id: number, request: SubprocesoRequest): Observable<Subproceso> {
    return this.http.put<Subproceso>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateEstado(id: number, nuevoEstado: EstadoDocumentacion): Observable<Subproceso> {
    const params = new HttpParams().set('nuevoEstado', nuevoEstado);
    return this.http.patch<Subproceso>(`${this.apiUrl}/${id}/estado`, null, { params });
  }
}