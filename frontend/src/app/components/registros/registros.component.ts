import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ButtonModule } from 'primeng/button';
import { TooltipModule } from 'primeng/tooltip';

// Wrappers
import {
  ButtonAddComponent,
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
    ToastModule,
    ConfirmDialogModule,
    ButtonModule,
    TooltipModule,
    ButtonAddComponent,
    PanelComponent,
    DataTableComponent,
    LoadingSpinnerComponent
  ],
  templateUrl: './registros.component.html',
  styleUrl: './registros.component.css',
  providers: [MessageService, ConfirmationService]
})
export class RegistrosComponent implements OnInit {
  cargando: boolean = false;
  documentos: InformacionDocumentada[] = [];
  documentosMatriz: InformacionDocumentada[] = [];
  documentosLatacunga: InformacionDocumentada[] = [];
  documentosSantoDomingo: InformacionDocumentada[] = [];

  // Configuración de columnas
  columnas: Column[] = [
    { field: 'fechaSolicitud', header: 'Fecha Solicitud', sortable: true },
    { field: 'unidad', header: 'Unidad', sortable: true },
    { field: 'solicitadoPor', header: 'Solicitado Por', sortable: true },
    { field: 'sede', header: 'Sede', sortable: true },
    { field: 'macroprocesoNombre', header: 'Macroproceso', sortable: true },
    { field: 'procesoN1Nombre', header: 'Proceso N1', sortable: true },
    { field: 'procesoN2Nombre', header: 'Proceso N2', sortable: true },
    { field: 'subprocesoN1Nombre', header: 'Subproceso N1', sortable: true },
    { field: 'subprocesoN2Nombre', header: 'Subproceso N2', sortable: true },
    { field: 'tipoDocumento', header: 'Tipo Documento', sortable: true },
    { field: 'nombreDocumento', header: 'Nombre Documento', sortable: true },
    { field: 'fechaProtocolo', header: 'Fecha Protocolo', sortable: true },
    { field: 'lugarEvento', header: 'Lugar Evento', sortable: true },
    { field: 'enlaceArchivo', header: 'Enlace', sortable: false },
    { field: 'motivo', header: 'Motivo', sortable: true },
    { field: 'observacionesUpdi', header: 'Obs. UPDI', sortable: false },
    { field: 'codificadoPor', header: 'Codificado Por', sortable: true },
    { field: 'codigoDocumento', header: 'Código Documento', sortable: true },
    { field: 'codigoProceso', header: 'Código Proceso', sortable: true },
    { field: 'estado', header: 'Estado', sortable: true },
    { field: 'fechaEliminacion', header: 'Fecha Eliminación', sortable: true },
    { field: 'mes', header: 'Mes', sortable: true }
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
      error: (error: any) => {
        console.error('Error al cargar documentos:', error);
        this.mostrarError('No se pudieron cargar los documentos');
        this.cargando = false;
      }
    });
  }

  filtrarPorSedes(): void {
    this.documentosMatriz = this.documentos.filter(d => d.sede === 'MATRIZ');
    this.documentosLatacunga = this.documentos.filter(d => d.sede === 'LATACUNGA');
    this.documentosSantoDomingo = this.documentos.filter(d => d.sede === 'SANTO_DOMINGO');
  }

  confirmarEliminar(documento: InformacionDocumentada): void {
    this.confirmationService.confirm({
      message: '¿Está seguro que desea eliminar este documento?',
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.eliminar(documento.id!);
      }
    });
  }

  eliminar(id: number): void {
    this.cargando = true;
    this.informacionDocumentadaService.eliminar(id).subscribe({
      next: () => {
        this.mostrarExito('Documento eliminado correctamente');
        this.cargarDatos();
      },
      error: (error: any) => {
        console.error('Error al eliminar:', error);
        this.mostrarError('No se pudo eliminar el documento');
        this.cargando = false;
      }
    });
  }

  formatearFecha(fecha: Date | string | undefined): string {
    if (!fecha) return 'N/A';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-EC');
  }

  // Métodos para mensajes
  mostrarExito(mensaje: string): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Éxito',
      detail: mensaje
    });
  }

  mostrarError(mensaje: string): void {
    this.messageService.add({
      severity: 'error',
      summary: 'Error',
      detail: mensaje
    });
  }
}