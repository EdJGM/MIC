import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SubprocesoService } from '../../services/subproceso.service';
import { ProcesoService } from '../../services/proceso.service';
import {
  Subproceso,
  SubprocesoRequest,
  EstadoDocumentacion,
  Proceso
} from '../../models/inventario.model';

// PrimeNG Imports
import { TableModule } from 'primeng/table';
import { MultiSelectModule } from 'primeng/multiselect';
import { ProgressBarModule } from 'primeng/progressbar';
import { TagModule } from 'primeng/tag';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService } from 'primeng/api';

// Shared Components
import {
  ButtonPrimaryComponent,
  ButtonCancelComponent,
  ButtonAddComponent,
  InputTextComponent,
  TextareaComponent,
  DropdownComponent,
  PanelComponent,
  DataTableComponent
} from '../../shared';

interface Column {
  field: string;
  header: string;
  sortable?: boolean;
}

@Component({
  selector: 'app-subprocesos-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    MultiSelectModule,
    ProgressBarModule,
    TagModule,
    CardModule,
    ToastModule,
    ConfirmDialogModule,
    ButtonPrimaryComponent,
    ButtonCancelComponent,
    ButtonAddComponent,
    InputTextComponent,
    TextareaComponent,
    DropdownComponent,
    PanelComponent,
    DataTableComponent
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './subprocresos.component.html',
  styleUrls: ['./subprocresos.component.css']
})
export class SubprocesosListComponent implements OnInit {
  subprocesos: Subproceso[] = [];
  subprocesoSeleccionado: Subproceso | null = null;
  modoEdicion = false;
  mostrarFormulario = false;
  cargando = false;

  // Formulario
  formulario: SubprocesoRequest = this.inicializarFormulario();

  // Enumeraciones
  estadosDocumentacion = Object.values(EstadoDocumentacion);

  // Options para dropdowns
  estadosOptions: { label: string; value: string }[] = [];
  procesosOptions: { label: string; value: number }[] = [];

  // Column toggle
  cols: Column[] = [
    { field: 'codigo', header: 'Código', sortable: true },
    { field: 'nombre', header: 'Nombre', sortable: true },
    { field: 'procesoNombre', header: 'Proceso', sortable: true },
    { field: 'estadoDocumentacion', header: 'Estado', sortable: true },
    { field: 'porcentajeAvance', header: 'Avance %', sortable: true },
    { field: 'fechaCreacion', header: 'Fecha Creación', sortable: true }
  ];
  selectedColumns: Column[] = [...this.cols];

  // Procesos para dropdown
  procesos: Proceso[] = [];

  constructor(
    private subprocesoService: SubprocesoService,
    private procesoService: ProcesoService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  ngOnInit(): void {
    this.initDropdownOptions();
    this.cargarDatosIniciales();
  }

  initDropdownOptions(): void {
    this.estadosOptions = this.estadosDocumentacion.map(estado => ({
      label: this.getEstadoNombre(estado),
      value: estado
    }));
  }

  cargarDatosIniciales(): void {
    this.cargando = true;
    Promise.all([
      this.cargarSubprocesos(),
      this.cargarProcesos()
    ]).finally(() => {
      this.cargando = false;
    });
  }

  cargarSubprocesos(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.subprocesoService.getAll().subscribe({
        next: (data) => {
          this.subprocesos = data;
          resolve();
        },
        error: (error) => {
          console.error('Error cargando subprocesos:', error);
          this.mostrarError('Error al cargar los subprocesos');
          reject(error);
        }
      });
    });
  }

  cargarProcesos(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.procesoService.getAll().subscribe({
        next: (data) => {
          this.procesos = data;
          this.procesosOptions = [
            { label: 'Seleccione un proceso', value: 0 },
            ...data.map(proc => ({
              label: `${proc.codigo} - ${proc.nombre}`,
              value: proc.id!
            }))
          ];
          resolve();
        },
        error: (error) => {
          console.error('Error cargando procesos:', error);
          this.mostrarError('Error al cargar los procesos');
          reject(error);
        }
      });
    });
  }

  nuevoSubproceso(): void {
    this.modoEdicion = false;
    this.mostrarFormulario = true;
    this.formulario = this.inicializarFormulario();
    this.subprocesoSeleccionado = null;
  }

  editarSubproceso(subproceso: Subproceso): void {
    this.modoEdicion = true;
    this.mostrarFormulario = true;
    this.subprocesoSeleccionado = subproceso;
    this.formulario = {
      procesoId: subproceso.procesoId,
      nombre: subproceso.nombre,
      descripcion: subproceso.descripcion,
      estadoDocumentacion: subproceso.estadoDocumentacion
    };
  }

  guardar(): void {
    if (this.formulario.procesoId === 0) {
      this.mostrarAdvertencia('Debe seleccionar un proceso');
      return;
    }

    this.cargando = true;
    if (this.modoEdicion && this.subprocesoSeleccionado) {
      this.subprocesoService.update(this.subprocesoSeleccionado.id!, this.formulario).subscribe({
        next: () => {
          this.mostrarExito('Subproceso actualizado exitosamente');
          this.cargarSubprocesos();
          this.cancelar();
        },
        error: (error) => {
          console.error('Error actualizando subproceso:', error);
          this.mostrarError('Error al actualizar el subproceso');
        },
        complete: () => {
          this.cargando = false;
        }
      });
    } else {
      this.subprocesoService.create(this.formulario).subscribe({
        next: () => {
          this.mostrarExito('Subproceso creado exitosamente');
          this.cargarSubprocesos();
          this.cancelar();
        },
        error: (error) => {
          console.error('Error creando subproceso:', error);
          this.mostrarError('Error al crear el subproceso');
        },
        complete: () => {
          this.cargando = false;
        }
      });
    }
  }

  eliminar(subproceso: Subproceso): void {
    this.confirmationService.confirm({
      message: `¿Está seguro de eliminar el subproceso "${subproceso.nombre}"?`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.cargando = true;
        this.subprocesoService.delete(subproceso.id!).subscribe({
          next: () => {
            this.mostrarExito('Subproceso eliminado exitosamente');
            this.cargarSubprocesos();
          },
          error: (error) => {
            console.error('Error eliminando subproceso:', error);
            this.mostrarError('Error al eliminar el subproceso');
          },
          complete: () => {
            this.cargando = false;
          }
        });
      }
    });
  }

  cancelar(): void {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.subprocesoSeleccionado = null;
    this.formulario = this.inicializarFormulario();
  }

  private inicializarFormulario(): SubprocesoRequest {
    return {
      procesoId: 0,
      nombre: '',
      descripcion: '',
      estadoDocumentacion: EstadoDocumentacion.NO_DOCUMENTADO
    };
  }

  // Utilidades
  getEstadoSeverity(estado: EstadoDocumentacion): "success" | "secondary" | "info" | "warning" | "danger" | "contrast" | undefined {
    const severityMap: { [key in EstadoDocumentacion]: "success" | "secondary" | "info" | "warning" | "danger" | "contrast" } = {
      [EstadoDocumentacion.NO_DOCUMENTADO]: 'secondary',
      [EstadoDocumentacion.LEVANTAMIENTO]: 'info',
      [EstadoDocumentacion.FLUJODIAGRAMACION]: 'info',
      [EstadoDocumentacion.CARACTERIZACION]: 'warning',
      [EstadoDocumentacion.VALIDACION]: 'warning',
      [EstadoDocumentacion.LEGALIZADO]: 'success',
      [EstadoDocumentacion.DIFUNDIDO]: 'success',
      [EstadoDocumentacion.MEJORA]: 'contrast'
    };
    return severityMap[estado];
  }

  getEstadoNombre(estado: EstadoDocumentacion): string {
    const nombreMap: { [key in EstadoDocumentacion]: string } = {
      [EstadoDocumentacion.NO_DOCUMENTADO]: 'No Documentado',
      [EstadoDocumentacion.LEVANTAMIENTO]: 'Levantamiento',
      [EstadoDocumentacion.FLUJODIAGRAMACION]: 'Flujodiagramación',
      [EstadoDocumentacion.CARACTERIZACION]: 'Caracterización',
      [EstadoDocumentacion.VALIDACION]: 'Validación',
      [EstadoDocumentacion.LEGALIZADO]: 'Legalizado',
      [EstadoDocumentacion.DIFUNDIDO]: 'Difundido',
      [EstadoDocumentacion.MEJORA]: 'Mejora Continua'
    };
    return nombreMap[estado];
  }

  formatFecha(fecha: string | undefined): string {
    if (!fecha) return 'N/A';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-EC', {
      year: 'numeric',
      month: 'short',
      day: '2-digit'
    });
  }

  isColumnVisible(field: string): boolean {
    return this.selectedColumns.some(col => col.field === field);
  }

  // Mensajes Toast
  private mostrarExito(mensaje: string): void {
    this.messageService.add({
      severity: 'success',
      summary: 'Éxito',
      detail: mensaje,
      life: 3000
    });
  }

  private mostrarError(mensaje: string): void {
    this.messageService.add({
      severity: 'error',
      summary: 'Error',
      detail: mensaje,
      life: 5000
    });
  }

  private mostrarAdvertencia(mensaje: string): void {
    this.messageService.add({
      severity: 'warn',
      summary: 'Advertencia',
      detail: mensaje,
      life: 4000
    });
  }
}