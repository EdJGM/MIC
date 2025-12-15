import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  Macroproceso, 
  MacroprocesoRequest, 
  EstadoDocumentacion 
} from '../models/inventario.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MacroprocesoService {
  private apiUrl = `${environment.apiUrl}/macroprocesos`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<Macroproceso[]> {
    return this.http.get<Macroproceso[]>(this.apiUrl);
  }

  getById(id: number): Observable<Macroproceso> {
    return this.http.get<Macroproceso>(`${this.apiUrl}/${id}`);
  }

  create(request: MacroprocesoRequest): Observable<Macroproceso> {
    return this.http.post<Macroproceso>(this.apiUrl, request);
  }

  update(id: number, request: MacroprocesoRequest): Observable<Macroproceso> {
    return this.http.put<Macroproceso>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateEstado(id: number, nuevoEstado: EstadoDocumentacion): Observable<Macroproceso> {
    const params = new HttpParams().set('nuevoEstado', nuevoEstado);
    return this.http.patch<Macroproceso>(`${this.apiUrl}/${id}/estado`, null, { params });
  }
}
