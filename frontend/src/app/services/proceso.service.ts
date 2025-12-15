import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  Proceso, 
  ProcesoRequest, 
  EstadoDocumentacion 
} from '../models/inventario.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProcesoService {
  private apiUrl = `${environment.apiUrl}/procesos`;

  constructor(private http: HttpClient) { }

  getAll(): Observable<Proceso[]> {
    return this.http.get<Proceso[]>(this.apiUrl);
  }

  getById(id: number): Observable<Proceso> {
    return this.http.get<Proceso>(`${this.apiUrl}/${id}`);
  }

  getByMacroproceso(macroprocesoId: number): Observable<Proceso[]> {
    return this.http.get<Proceso[]>(`${this.apiUrl}/macroproceso/${macroprocesoId}`);
  }

  create(request: ProcesoRequest): Observable<Proceso> {
    return this.http.post<Proceso>(this.apiUrl, request);
  }

  update(id: number, request: ProcesoRequest): Observable<Proceso> {
    return this.http.put<Proceso>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateEstado(id: number, nuevoEstado: EstadoDocumentacion): Observable<Proceso> {
    const params = new HttpParams().set('nuevoEstado', nuevoEstado);
    return this.http.patch<Proceso>(`${this.apiUrl}/${id}/estado`, null, { params });
  }
}
