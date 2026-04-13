import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InformacionDocumentada, InformacionDocumentadaRequest } from '../models/informacion-documentada.model';

@Injectable({
  providedIn: 'root'
})
export class InformacionDocumentadaService {
  private apiUrl = 'http://localhost:8081/api/informacion-documentada';

  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  crear(informacionDocumentada: InformacionDocumentadaRequest): Observable<InformacionDocumentada> {
    return this.http.post<InformacionDocumentada>(this.apiUrl, informacionDocumentada, this.httpOptions);
  }

  listarTodos(): Observable<InformacionDocumentada[]> {
    return this.http.get<InformacionDocumentada[]>(this.apiUrl);
  }

  listarPorSede(sede: string): Observable<InformacionDocumentada[]> {
    return this.http.get<InformacionDocumentada[]>(`${this.apiUrl}/sede/${sede}`);
  }

  obtenerPorId(id: number): Observable<InformacionDocumentada> {
    return this.http.get<InformacionDocumentada>(`${this.apiUrl}/${id}`);
  }

  actualizar(id: number, informacionDocumentada: InformacionDocumentadaRequest): Observable<InformacionDocumentada> {
    return this.http.put<InformacionDocumentada>(`${this.apiUrl}/${id}`, informacionDocumentada, this.httpOptions);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  nuevaVersion(id: number, request: { enlaceArchivo: string; observaciones?: string }): Observable<InformacionDocumentada> {
    return this.http.post<InformacionDocumentada>(`${this.apiUrl}/${id}/nueva-version`, request, this.httpOptions);
  }

  getHistorialVersiones(id: number): Observable<InformacionDocumentada[]> {
    return this.http.get<InformacionDocumentada[]>(`${this.apiUrl}/${id}/historial`);
  }
}
