import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProcesoService } from '../../services/proceso.service';
import { MacroprocesoService } from '../../services/macroproceso.service';
import {
  Proceso,
  ProcesoRequest,
  EstadoDocumentacion,
  Macroproceso
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
  templateUrl: './procresos.component.html',
  styleUrls: ['./procresos.component.css']
})
export class ProcesosListComponent implements OnInit {
  procesos: Proceso[] = [];
  procesoSeleccionado: Proceso | null = null;
  modoEdicion = false;
  mostrarFormulario = false;
  cargando = false;

  // Formulario
  formulario: ProcesoRequest = this.inicializarFormulario();

  // Enumeraciones
  estadosDocumentacion = Object.values(EstadoDocumentacion);

  // Options para dropdowns
  estadosOptions: { label: string; value: string }[] = [];
  macroprocesosOptions: { label: string; value: number }[] = [];

  // Column toggle
  cols: Column[] = [
    { field: 'codigo', header: 'Código', sortable: true },
    { field: 'nombre', header: 'Nombre', sortable: true },
    { field: 'macroprocesoNombre', header: 'Macroproceso', sortable: true },
    { field: 'responsable', header: 'Responsable', sortable: true },
    { field: 'estadoDocumentacion', header: 'Estado', sortable: true },
    { field: 'porcentajeAvance', header: 'Avance %', sortable: true },
    { field: 'cantidadSubprocesos', header: 'N° Subprocesos', sortable: true }
  ];
  selectedColumns: Column[] = [...this.cols];

  // Macroprocesos para dropdown
  macroprocesos: Macroproceso[] = [];

  constructor(
    private procesoService: ProcesoService,
    private macroprocesoService: MacroprocesoService,
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
      this.cargarProcesos(),
      this.cargarMacroprocesos()
    ]).finally(() => {
      this.cargando = false;
    });
  }

  cargarProcesos(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.procesoService.getAll().subscribe({
        next: (data) => {
          this.procesos = data;
          resolve();
        },
        error: (error) => {
          this.mostrarError('Error al cargar los procesos');
          reject(error);
        }
      });
    });
  }

  cargarMacroprocesos(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.macroprocesoService.getAll().subscribe({
        next: (data) => {
          this.macroprocesos = data;
          this.macroprocesosOptions = [
            { label: 'Seleccione un macroproceso', value: 0 },
            ...data.map(mp => ({
              label: `${mp.codigo} - ${mp.nombre}`,
              value: mp.id!
            }))
          ];
          resolve();
        },
        error: (error) => {
          this.mostrarError('Error al cargar los macroprocesos');
          reject(error);
        }
      });
    });
  }

  nuevoProceso(): void {
    this.modoEdicion = false;
    this.mostrarFormulario = true;
    this.formulario = this.inicializarFormulario();
    this.procesoSeleccionado = null;
  }

  editarProceso(proceso: Proceso): void {
    this.modoEdicion = true;
    this.mostrarFormulario = true;
    this.procesoSeleccionado = proceso;
    this.formulario = {
      macroprocesoId: proceso.macroprocesoId,
      nombre: proceso.nombre,
      descripcion: proceso.descripcion,
      responsable: proceso.responsable,
      estadoDocumentacion: proceso.estadoDocumentacion
    };
  }

  guardar(): void {
    if (this.formulario.macroprocesoId === 0) {
      this.mostrarAdvertencia('Debe seleccionar un macroproceso');
      return;
    }

    this.cargando = true;
    if (this.modoEdicion && this.procesoSeleccionado) {
      this.procesoService.update(this.procesoSeleccionado.id!, this.formulario).subscribe({
        next: () => {
          this.mostrarExito('Proceso actualizado exitosamente');
          this.cargarProcesos();
          this.cancelar();
        },
        error: (error) => {
          this.mostrarError('Error al actualizar el proceso');
        },
        complete: () => {
          this.cargando = false;
        }
      });
    } else {
      this.procesoService.create(this.formulario).subscribe({
        next: () => {
          this.mostrarExito('Proceso creado exitosamente');
          this.cargarProcesos();
          this.cancelar();
        },
        error: (error) => {
          this.mostrarError('Error al crear el proceso');
        },
        complete: () => {
          this.cargando = false;
        }
      });
    }
  }

  eliminar(proceso: Proceso): void {
    this.confirmationService.confirm({
      message: `¿Está seguro de eliminar el proceso "${proceso.nombre}"?`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.cargando = true;
        this.procesoService.delete(proceso.id!).subscribe({
          next: () => {
            this.mostrarExito('Proceso eliminado exitosamente');
            this.cargarProcesos();
          },
          error: (error) => {
            this.mostrarError('Error al eliminar. Verifique que no tenga subprocesos asociados.');
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
    this.procesoSeleccionado = null;
    this.formulario = this.inicializarFormulario();
  }

  private inicializarFormulario(): ProcesoRequest {
    return {
      macroprocesoId: 0,
      nombre: '',
      descripcion: '',
      responsable: '',
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