import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuditLogService } from '../../services/audit-log.service';
import { AuditLog, ACCION_AUDIT_OPTIONS } from '../../models/configuracion.model';

import { TagModule } from 'primeng/tag';

import {
  ButtonPrimaryComponent,
  ButtonCancelComponent,
  ButtonSecondaryComponent,
  DropdownComponent,
  CalendarComponent,
  PanelComponent,
  DataTableComponent
} from '../../shared';

@Component({
  selector: 'app-configuracion-auditoria',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TagModule,
    ButtonPrimaryComponent,
    ButtonCancelComponent,
    ButtonSecondaryComponent,
    DropdownComponent,
    CalendarComponent,
    PanelComponent,
    DataTableComponent
  ],
  templateUrl: './configuracion-auditoria.component.html',
  styleUrls: ['./configuracion-auditoria.component.css']
})
export class ConfiguracionAuditoriaComponent implements OnInit {
  logs: AuditLog[] = [];

  filtroModulo: string = '';
  filtroDesde: Date | null = null;
  filtroHasta: Date | null = null;

  cols = [
    { field: 'fechaHora',        header: 'Fecha/Hora' },
    { field: 'usuarioNombre',    header: 'Usuario' },
    { field: 'accionDescripcion',header: 'Acción' },
    { field: 'modulo',           header: 'Módulo' },
    { field: 'descripcion',      header: 'Descripción' },
    { field: 'resultado',        header: 'Resultado' },
    { field: 'ip',               header: 'IP' }
  ];

  modulosOpciones = [
    { label: 'Todos', value: '' },
    { label: 'Usuario Local', value: 'USUARIO_LOCAL' },
    { label: 'Parámetro', value: 'PARAMETRO' },
    { label: 'Macroproceso', value: 'MACROPROCESO' },
    { label: 'Proceso', value: 'PROCESO' },
    { label: 'Subproceso', value: 'SUBPROCESO' }
  ];

  constructor(private auditLogService: AuditLogService) {}

  ngOnInit(): void {
    this.cargarLogs();
  }

  cargarLogs(): void {
    const desde = this.filtroDesde ? this.filtroDesde.toISOString() : undefined;
    const hasta = this.filtroHasta ? this.filtroHasta.toISOString() : undefined;
    const modulo = this.filtroModulo || undefined;

    const observable = (modulo || desde || hasta)
      ? this.auditLogService.filtrar(modulo, desde, hasta)
      : this.auditLogService.getAll();

    observable.subscribe({
      next: (data) => {
        this.logs = data;
      },
      error: (error) => {
        console.error('Error cargando logs de auditoría:', error);
        alert('Error al cargar el log de auditoría');
      }
    });
  }

  limpiarFiltros(): void {
    this.filtroModulo = '';
    this.filtroDesde = null;
    this.filtroHasta = null;
    this.cargarLogs();
  }

  getResultadoSeverity(resultado: string | undefined): 'success' | 'danger' {
    return resultado === 'SUCCESS' ? 'success' : 'danger';
  }

  getAccionSeverity(accion: string | undefined): 'success' | 'info' | 'warning' | 'danger' {
    const map: Record<string, 'success' | 'info' | 'warning' | 'danger'> = {
      CREATE: 'success',
      UPDATE: 'info',
      DELETE: 'danger',
      READ: 'info',
      ACTIVATE: 'success',
      DEACTIVATE: 'warning'
    };
    return map[accion ?? ''] ?? 'info';
  }
}
