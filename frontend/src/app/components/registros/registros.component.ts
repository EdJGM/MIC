import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';
import { DialogModule } from 'primeng/dialog';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';

// Wrappers
import {
  ButtonAddComponent,
  ButtonPrimaryComponent,
  ButtonCancelComponent,
  InputTextComponent,
  TextareaComponent,
  PanelComponent,
  DataTableComponent,
  LoadingSpinnerComponent
} from '../../shared';

// Servicios
import { InformacionDocumentadaService } from '../../services/informacion-documentada.service';

// Modelos
import { InformacionDocumentada } from '../../models/informacion-documentada.model';

interface Column {
  field: string;
  header: string;
  sortable?: boolean;
}

@Component({
  selector: 'app-registros',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ToastModule,
    ConfirmDialogModule,
    ButtonModule,
    TooltipModule,
    DialogModule,
    TableModule,
    TagModule,
    ButtonAddComponent,
    ButtonPrimaryComponent,
    ButtonCancelComponent,
    InputTextComponent,
    TextareaComponent,
    PanelComponent,
    DataTableComponent,
    LoadingSpinnerComponent
  ],
  templateUrl: './registros.component.html',
  styleUrl: './registros.component.css',
  providers: [MessageService, ConfirmationService]
})
export class RegistrosComponent implements OnInit {
  cargando = false;
  documentos: InformacionDocumentada[] = [];
  documentosMatriz: InformacionDocumentada[] = [];
  documentosLatacunga: InformacionDocumentada[] = [];
  documentosSantoDomingo: InformacionDocumentada[] = [];

  // --- Nueva versión ---
  mostrarDialogNuevaVersion = false;
  documentoParaVersion: InformacionDocumentada | null = null;
  nuevaVersionForm = { enlaceArchivo: '', observaciones: '' };
  guardandoVersion = false;

  // --- Historial de versiones ---
  mostrarDialogHistorial = false;
  historialVersiones: InformacionDocumentada[] = [];
  cargandoHistorial = false;
  documentoHistorialTitulo = '';

  // Configuración de columnas
  columnas: Column[] = [
    { field: 'version',          header: 'Versión',          sortable: true },
    { field: 'codigoDocumento',  header: 'Código Documento', sortable: true },
    { field: 'nombreDocumento',  header: 'Nombre Documento', sortable: true },
    { field: 'tipoDocumento',    header: 'Tipo',             sortable: true },
    { field: 'unidad',           header: 'Unidad',           sortable: true },
    { field: 'solicitadoPor',    header: 'Solicitado Por',   sortable: true },
    { field: 'macroprocesoNombre', header: 'Macroproceso',   sortable: true },
    { field: 'procesoN1Nombre',  header: 'Proceso N1',       sortable: true },
    { field: 'procesoN2Nombre',  header: 'Proceso N2',       sortable: true },
    { field: 'fechaProtocolo',   header: 'Fecha Protocolo',  sortable: true },
    { field: 'enlaceArchivo',    header: 'Enlace',           sortable: false },
    { field: 'motivo',           header: 'Motivo',           sortable: true },
    { field: 'estado',           header: 'Estado',           sortable: true },
    { field: 'fechaSolicitud',   header: 'Fecha Registro',   sortable: true }
  ];

  colsHistorial: Column[] = [
    { field: 'version',         header: 'Versión' },
    { field: 'codigoDocumento', header: 'Código' },
    { field: 'enlaceArchivo',   header: 'Enlace' },
    { field: 'motivo',          header: 'Motivo' },
    { field: 'estado',          header: 'Estado' },
    { field: 'fechaSolicitud',  header: 'Fecha' }
  ];

  constructor(
    private informacionDocumentadaService: InformacionDocumentadaService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando = true;
    this.informacionDocumentadaService.listarTodos().subscribe({
      next: (data: InformacionDocumentada[]) => {
        this.documentos = data;
        this.filtrarPorSedes();
        this.cargando = false;
      },
      error: () => {
        this.mostrarError('No se pudieron cargar los documentos');
        this.cargando = false;
      }
    });
  }

  filtrarPorSedes(): void {
    this.documentosMatriz       = this.documentos.filter(d => d.sede === 'MATRIZ');
    this.documentosLatacunga    = this.documentos.filter(d => d.sede === 'LATACUNGA');
    this.documentosSantoDomingo = this.documentos.filter(d => d.sede === 'SANTO_DOMINGO');
  }

  // ── Nueva versión ─────────────────────────────────────────────────────────

  abrirNuevaVersion(documento: InformacionDocumentada): void {
    this.documentoParaVersion = documento;
    this.nuevaVersionForm = { enlaceArchivo: '', observaciones: '' };
    this.mostrarDialogNuevaVersion = true;
  }

  guardarNuevaVersion(): void {
    if (!this.nuevaVersionForm.enlaceArchivo.trim()) {
      this.mostrarAdvertencia('El enlace al archivo es obligatorio');
      return;
    }
    this.guardandoVersion = true;
    this.informacionDocumentadaService
      .nuevaVersion(this.documentoParaVersion!.id!, this.nuevaVersionForm)
      .subscribe({
        next: () => {
          this.mostrarExito('Nueva versión creada exitosamente');
          this.mostrarDialogNuevaVersion = false;
          this.cargarDatos();
          this.guardandoVersion = false;
        },
        error: () => {
          this.mostrarError('No se pudo crear la nueva versión');
          this.guardandoVersion = false;
        }
      });
  }

  cancelarNuevaVersion(): void {
    this.mostrarDialogNuevaVersion = false;
    this.documentoParaVersion = null;
  }

  // ── Historial de versiones ─────────────────────────────────────────────────

  verHistorial(documento: InformacionDocumentada): void {
    this.documentoHistorialTitulo = documento.nombreDocumento;
    this.cargandoHistorial = true;
    this.mostrarDialogHistorial = true;
    this.informacionDocumentadaService.getHistorialVersiones(documento.id!).subscribe({
      next: (data) => {
        this.historialVersiones = data;
        this.cargandoHistorial = false;
      },
      error: () => {
        this.mostrarError('No se pudo cargar el historial');
        this.cargandoHistorial = false;
      }
    });
  }

  // ── Eliminar ──────────────────────────────────────────────────────────────

  confirmarEliminar(documento: InformacionDocumentada): void {
    this.confirmationService.confirm({
      message: `¿Está seguro que desea eliminar el documento "${documento.nombreDocumento}"?`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.eliminar(documento.id!)
    });
  }

  eliminar(id: number): void {
    this.cargando = true;
    this.informacionDocumentadaService.eliminar(id).subscribe({
      next: () => {
        this.mostrarExito('Documento eliminado correctamente');
        this.cargarDatos();
      },
      error: () => {
        this.mostrarError('No se pudo eliminar el documento');
        this.cargando = false;
      }
    });
  }

  // ── Utilidades ────────────────────────────────────────────────────────────

  formatearFecha(fecha: Date | string | undefined): string {
    if (!fecha) return 'N/A';
    return new Date(fecha).toLocaleDateString('es-EC');
  }

  getVersionSeverity(version: string | undefined): 'success' | 'info' | 'warning' | 'secondary' {
    if (!version) return 'secondary';
    const num = parseInt(version.substring(1), 10);
    if (num === 1) return 'info';
    if (num === 2) return 'warning';
    return 'success';
  }

  getEstadoSeverity(estado: string | undefined): 'success' | 'danger' | 'secondary' {
    if (estado === 'ACTIVO') return 'success';
    if (estado === 'OBSOLETO') return 'danger';
    return 'secondary';
  }

  getMotivoSeverity(motivo: string | undefined): 'info' | 'warning' | 'danger' | 'secondary' {
    if (motivo === 'CREACION')     return 'info';
    if (motivo === 'ACTUALIZACION') return 'warning';
    if (motivo === 'ELIMINACION')  return 'danger';
    return 'secondary';
  }

  private mostrarExito(mensaje: string): void {
    this.messageService.add({ severity: 'success', summary: 'Éxito', detail: mensaje, life: 3000 });
  }

  private mostrarError(mensaje: string): void {
    this.messageService.add({ severity: 'error', summary: 'Error', detail: mensaje, life: 5000 });
  }

  private mostrarAdvertencia(mensaje: string): void {
    this.messageService.add({ severity: 'warn', summary: 'Advertencia', detail: mensaje, life: 4000 });
  }
}
