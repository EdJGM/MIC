import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Notificacion } from '../models/configuracion.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NotificacionService {
  private apiUrl = `${environment.apiUrl}/notificaciones`;

  constructor(private http: HttpClient) { }

  getPorUsuario(destinatarioId: string): Observable<Notificacion[]> {
    const params = new HttpParams().set('destinatarioId', destinatarioId);
    return this.http.get<Notificacion[]>(this.apiUrl, { params });
  }

  getNoLeidas(destinatarioId: string): Observable<Notificacion[]> {
    const params = new HttpParams().set('destinatarioId', destinatarioId);
    return this.http.get<Notificacion[]>(`${this.apiUrl}/no-leidas`, { params });
  }

  contarNoLeidas(destinatarioId: string): Observable<{ noLeidas: number }> {
    const params = new HttpParams().set('destinatarioId', destinatarioId);
    return this.http.get<{ noLeidas: number }>(`${this.apiUrl}/contador`, { params });
  }

  marcarComoLeida(id: number): Observable<Notificacion> {
    return this.http.patch<Notificacion>(`${this.apiUrl}/${id}/leida`, {});
  }

  marcarTodasComoLeidas(destinatarioId: string): Observable<void> {
    const params = new HttpParams().set('destinatarioId', destinatarioId);
    return this.http.patch<void>(`${this.apiUrl}/marcar-todas-leidas`, {}, { params });
  }
}
