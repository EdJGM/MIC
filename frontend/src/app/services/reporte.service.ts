import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface GrupoConteo {
  grupo: string;
  cantidad: number;
  porcentaje: number;
}
export interface ReporteProcesosEstado {
  porEstado: GrupoConteo[];
  totalProcesos: number;
  promedioAvance: number;
}
export interface ReporteProcesosNivel {
  totalMacroprocesos: number;
  totalProcesosN1: number;
  totalProcesosN2: number;
  totalSubprocesosN1: number;
  totalSubprocesosN2: number;
  macroporcentajes: GrupoConteo[];
}
export interface ReporteDocumentosEstado {
  activos: number; suspendidos: number; obsoletos: number; total: number;
}
export interface ReporteDocumentosTipo {
  porTipo: GrupoConteo[]; total: number;
}
export interface ReporteDocumentosUnidad {
  porUnidad: GrupoConteo[]; total: number;
}
export interface ReporteDocumentosFecha {
  porMes: GrupoConteo[]; total: number;
  fechaDesde: string; fechaHasta: string;
}

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private api = `${environment.apiUrl}/reportes`;
  constructor(private http: HttpClient) {}

  // RF-28
  procesosPorEstado(unidad?: string, fechaDesde?: string, fechaHasta?: string): Observable<ReporteProcesosEstado> {
    let params = new HttpParams();
    if (unidad)     params = params.set('unidad', unidad);
    if (fechaDesde) params = params.set('fechaDesde', fechaDesde);
    if (fechaHasta) params = params.set('fechaHasta', fechaHasta);
    return this.http.get<ReporteProcesosEstado>(`${this.api}/procesos/estado`, { params });
  }
  // RF-29
  procesosPorNivel(fechaDesde?: string, fechaHasta?: string): Observable<ReporteProcesosNivel> {
    let params = new HttpParams();
    if (fechaDesde) params = params.set('fechaDesde', fechaDesde);
    if (fechaHasta) params = params.set('fechaHasta', fechaHasta);
    return this.http.get<ReporteProcesosNivel>(`${this.api}/procesos/nivel`, { params });
  }
  // RF-30
  procesosPorUnidad(fechaDesde?: string, fechaHasta?: string): Observable<GrupoConteo[]> {
    let params = new HttpParams();
    if (fechaDesde) params = params.set('fechaDesde', fechaDesde);
    if (fechaHasta) params = params.set('fechaHasta', fechaHasta);
    return this.http.get<GrupoConteo[]>(`${this.api}/procesos/unidad`, { params });
  }
  // RF-24
  documentosPorEstado(): Observable<ReporteDocumentosEstado> {
    return this.http.get<ReporteDocumentosEstado>(`${this.api}/documentos/estado`);
  }
  // RF-25
  documentosPorTipo(): Observable<ReporteDocumentosTipo> {
    return this.http.get<ReporteDocumentosTipo>(`${this.api}/documentos/tipo`);
  }
  // RF-26
  documentosPorUnidad(): Observable<ReporteDocumentosUnidad> {
    return this.http.get<ReporteDocumentosUnidad>(`${this.api}/documentos/unidad`);
  }
  // RF-27
  documentosPorFecha(anioDesde?: number, anioHasta?: number): Observable<ReporteDocumentosFecha> {
    let params = new HttpParams();
    if (anioDesde) params = params.set('anioDesde', anioDesde);
    if (anioHasta) params = params.set('anioHasta', anioHasta);
    return this.http.get<ReporteDocumentosFecha>(`${this.api}/documentos/fecha`, { params });
  }
}
