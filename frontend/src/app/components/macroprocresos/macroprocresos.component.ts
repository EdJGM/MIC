import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MacroprocesoService } from '../../services/macroproceso.service';
import { ObjetivoEspecificoService } from '../../services/objetivo-especifico.service';
import {
  Macroproceso,
  MacroprocesoRequest,
  TipoMacroproceso,
  EstadoDocumentacion,
  ObjetivoEspecifico
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

// Shared Components (Wrappers)
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
  templateUrl: './macroprocresos.component.html',
  styleUrls: ['./macroprocresos.component.css']
})
export class MacroprocesosListComponent implements OnInit {
  macroprocesos: Macroproceso[] = [];
  macroprocesoSeleccionado: Macroproceso | null = null;
  modoEdicion = false;
  mostrarFormulario = false;
  cargando = false;

  // Formulario
  formulario: MacroprocesoRequest = this.inicializarFormulario();

  // Enumeraciones
  tiposMacroproceso = Object.values(TipoMacroproceso);
  estadosDocumentacion = Object.values(EstadoDocumentacion);

  // Options para dropdowns
  tiposOptions: { label: string; value: string }[] = [];
  estadosOptions: { label: string; value: string }[] = [];
  objetivosOptions: { label: string; value: string }[] = [];

  // Column toggle con sortable
  cols: Column[] = [
    { field: 'codigo', header: 'Código', sortable: true },
    { field: 'tipo', header: 'Tipo', sortable: true },
    { field: 'nombre', header: 'Nombre', sortable: true },
    { field: 'unidadEstrategica', header: 'Unidad Estratégica', sortable: true },
    { field: 'responsablePrincipal', header: 'Responsable', sortable: true },
    { field: 'estadoDocumentacion', header: 'Estado', sortable: true },
    { field: 'porcentajeAvance', header: 'Avance %', sortable: true },
    { field: 'cantidadProcesos', header: 'N° Procesos', sortable: true }
  ];
  selectedColumns: Column[] = [...this.cols]; // Todas seleccionadas por defecto

  // Objetivos específicos
  objetivosEspecificos: ObjetivoEspecifico[] = [];

  constructor(
    private macroprocesoService: MacroprocesoService,
    private objetivoEspecificoService: ObjetivoEspecificoService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  ngOnInit(): void {
    this.initDropdownOptions();
    this.cargarDatosIniciales();
  }

  initDropdownOptions(): void {
    this.tiposOptions = this.tiposMacroproceso.map(tipo => ({
      label: this.getTipoNombre(tipo),
      value: tipo
    }));

    this.estadosOptions = this.estadosDocumentacion.map(estado => ({
      label: this.getEstadoNombre(estado),
      value: estado
    }));
  }

  cargarDatosIniciales(): void {
    this.cargando = true;
    Promise.all([
      this.cargarMacroprocesos(),
      this.cargarObjetivosEspecificos()
    ]).finally(() => {
      this.cargando = false;
    });
  }

  cargarMacroprocesos(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.macroprocesoService.getAll().subscribe({
        next: (data) => {
          this.macroprocesos = data;
          resolve();
        },
        error: (error) => {
          console.error('Error cargando macroprocesos:', error);
          this.mostrarError('Error al cargar los macroprocesos');
          reject(error);
        }
      });
    });
  }

  cargarObjetivosEspecificos(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.objetivoEspecificoService.getAll().subscribe({
        next: (data) => {
          this.objetivosEspecificos = data;
          this.objetivosOptions = [
            { label: 'Seleccione un objetivo específico', value: '' },
            ...data.map(obj => ({ label: obj.nombre, value: obj.nombre }))
          ];
          resolve();
        },
        error: (error) => {
          this.mostrarError('Error al cargar los objetivos específicos');
          reject(error);
        }
      });
    });
  }

  nuevoMacroproceso(): void {
    this.modoEdicion = false;
    this.mostrarFormulario = true;
    this.formulario = this.inicializarFormulario();
    this.macroprocesoSeleccionado = null;
  }

  editarMacroproceso(macroproceso: Macroproceso): void {
    this.modoEdicion = true;
    this.mostrarFormulario = true;
    this.macroprocesoSeleccionado = macroproceso;
    this.formulario = {
      tipo: macroproceso.tipo,
      nombre: macroproceso.nombre,
      descripcion: macroproceso.descripcion,
      unidadEstrategica: macroproceso.unidadEstrategica,
      responsablePrincipal: macroproceso.responsablePrincipal,
      objetivosEstrategicos: macroproceso.objetivosEstrategicos,
      estadoDocumentacion: macroproceso.estadoDocumentacion
    };
  }

  guardar(): void {
    this.cargando = true;
    if (this.modoEdicion && this.macroprocesoSeleccionado) {
      this.macroprocesoService.update(this.macroprocesoSeleccionado.id!, this.formulario).subscribe({
        next: () => {
          this.mostrarExito('Macroproceso actualizado exitosamente');
          this.cargarMacroprocesos();
          this.cancelar();
        },
        error: (error) => {
          this.mostrarError('Error al actualizar el macroproceso');
        },
        complete: () => {
          this.cargando = false;
        }
      });
    } else {
      this.macroprocesoService.create(this.formulario).subscribe({
        next: () => {
          this.mostrarExito('Macroproceso creado exitosamente');
          this.cargarMacroprocesos();
          this.cancelar();
        },
        error: (error) => {
          this.mostrarError('Error al crear el macroproceso');
        },
        complete: () => {
          this.cargando = false;
        }
      });
    }
  }

  eliminar(macroproceso: Macroproceso): void {
    this.confirmationService.confirm({
      message: `¿Está seguro de eliminar el macroproceso "${macroproceso.nombre}"?`,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.cargando = true;
        this.macroprocesoService.delete(macroproceso.id!).subscribe({
          next: () => {
            this.mostrarExito('Macroproceso eliminado exitosamente');
            this.cargarMacroprocesos();
          },
          error: (error) => {
            this.mostrarError('Error al eliminar. Verifique que no tenga procesos asociados.');
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
    this.macroprocesoSeleccionado = null;
    this.formulario = this.inicializarFormulario();
  }

  private inicializarFormulario(): MacroprocesoRequest {
    return {
      tipo: TipoMacroproceso.VDC,
      nombre: '',
      descripcion: '',
      unidadEstrategica: '',
      responsablePrincipal: '',
      objetivosEstrategicos: '',
      estadoDocumentacion: EstadoDocumentacion.NO_DOCUMENTADO
    };
  }

  // Utilidades para visualización
  getTipoNombre(tipo: TipoMacroproceso): string {
    const nombreMap: { [key in TipoMacroproceso]: string } = {
      [TipoMacroproceso.REC]: 'Rectoría',
      [TipoMacroproceso.UTIC]: 'UTIC',
      [TipoMacroproceso.USGN]: 'USGN',
      [TipoMacroproceso.VDC]: 'Vicerrectorado Docencia',
      [TipoMacroproceso.VAD]: 'Vicerrectorado Académico',
      [TipoMacroproceso.VAG]: 'Vicerrectorado Administrativo',
      [TipoMacroproceso.VII]: 'Vicerrectorado Investigación'
    };
    return nombreMap[tipo];
  }

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

  getProgressBarColor(porcentaje: number): string {
    if (porcentaje === 0) return '#6c757d';
    if (porcentaje < 30) return '#dc3545';
    if (porcentaje < 60) return '#ffc107';
    if (porcentaje < 90) return '#17a2b8';
    return '#28a745';
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