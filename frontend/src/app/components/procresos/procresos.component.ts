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
  estadosOptions:        { label: string; value: string }[] = [];
  macroprocesosOptions:  { label: string; value: number }[] = [];
  nivelesOptions:        { label: string; value: number }[] = [
    { label: 'N1 — Proceso de primer nivel', value: 1 },
    { label: 'N2 — Proceso de segundo nivel (hijo de N1)', value: 2 }
  ];
  procesosN1Options: { label: string; value: number }[] = [];

  // Column toggle
  cols: Column[] = [
    { field: 'nivel',              header: 'Nivel',        sortable: true },
    { field: 'codigo',             header: 'Código',       sortable: true },
    { field: 'nombre',             header: 'Nombre',       sortable: true },
    { field: 'macroprocesoNombre', header: 'Macroproceso', sortable: true },
    { field: 'procesoPadreNombre', header: 'Proceso Padre (N1)', sortable: true },
    { field: 'estadoDocumentacion',header: 'Estado',       sortable: true },
    { field: 'porcentajeAvance',   header: 'Avance %',     sortable: true },
    { field: 'cantidadHijos',      header: 'N° Hijos',     sortable: false }
  ];
  selectedColumns: Column[] = this.cols.filter(c =>
    ['nivel','codigo','nombre','macroprocesoNombre','estadoDocumentacion','porcentajeAvance','cantidadHijos'].includes(c.field)
  );

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
    ]).finally(() => { this.cargando = false; });
  }

  cargarProcesos(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.procesoService.getAll().subscribe({
        next: (data) => { this.procesos = data; resolve(); },
        error: (err) => { this.mostrarError('Error al cargar los procesos'); reject(err); }
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
            ...data.map(mp => ({ label: `${mp.codigo} - ${mp.nombre}`, value: mp.id! }))
          ];
          resolve();
        },
        error: (err) => { this.mostrarError('Error al cargar los macroprocesos'); reject(err); }
      });
    });
  }

  /** Cuando cambia el macroproceso en el formulario: recarga N1 si estamos en nivel 2 */
  onMacroprocesoChange(macroprocesoId: number): void {
    this.formulario.procesoPadreId = undefined;
    this.procesosN1Options = [];
    if (this.formulario.nivel === 2 && macroprocesoId && macroprocesoId !== 0) {
      this.cargarProcesosN1(macroprocesoId);
    }
  }

  /** Cuando cambia el nivel en el formulario */
  onNivelChange(nivel: number): void {
    this.formulario.procesoPadreId = undefined;
    this.procesosN1Options = [];
    if (nivel === 2 && this.formulario.macroprocesoId && this.formulario.macroprocesoId !== 0) {
      this.cargarProcesosN1(this.formulario.macroprocesoId);
    }
  }

  private cargarProcesosN1(macroprocesoId: number): void {
    this.procesoService.getByMacroprocesoYNivel(macroprocesoId, 1).subscribe({
      next: (data) => {
        this.procesosN1Options = [
          { label: 'Seleccione proceso N1 padre', value: 0 },
          ...data.map(p => ({ label: `${p.codigo} - ${p.nombre}`, value: p.id! }))
        ];
      },
      error: () => this.mostrarError('Error al cargar los procesos N1')
    });
  }

  nuevoProceso(): void {
    this.modoEdicion = false;
    this.mostrarFormulario = true;
    this.formulario = this.inicializarFormulario();
    this.procesosN1Options = [];
    this.procesoSeleccionado = null;
  }

  editarProceso(proceso: Proceso): void {
    this.modoEdicion = true;
    this.mostrarFormulario = true;
    this.procesoSeleccionado = proceso;
    this.formulario = {
      macroprocesoId: proceso.macroprocesoId,
      nivel: proceso.nivel,
      procesoPadreId: proceso.procesoPadreId,
      nombre: proceso.nombre,
      descripcion: proceso.descripcion,
      objetivos: proceso.objetivos,
      estadoDocumentacion: proceso.estadoDocumentacion
    };
    // Si es N2, precargar opciones de proceso padre
    if (proceso.nivel === 2 && proceso.macroprocesoId) {
      this.cargarProcesosN1(proceso.macroprocesoId);
    }
  }

  guardar(): void {
    if (!this.formulario.macroprocesoId || this.formulario.macroprocesoId === 0) {
      this.mostrarAdvertencia('Debe seleccionar un macroproceso');
      return;
    }
    if (this.formulario.nivel === 2 && (!this.formulario.procesoPadreId || this.formulario.procesoPadreId === 0)) {
      this.mostrarAdvertencia('Debe seleccionar el Proceso N1 padre para un Proceso N2');
      return;
    }

    this.cargando = true;
    const op$ = this.modoEdicion && this.procesoSeleccionado
      ? this.procesoService.update(this.procesoSeleccionado.id!, this.formulario)
      : this.procesoService.create(this.formulario);

    op$.subscribe({
      next: () => {
        this.mostrarExito(this.modoEdicion ? 'Proceso actualizado exitosamente' : 'Proceso creado exitosamente');
        this.cargarProcesos();
        this.cancelar();
      },
      error: (err) => {
        const msg = err?.error?.message || (this.modoEdicion ? 'Error al actualizar' : 'Error al crear');
        this.mostrarError(msg);
        this.cargando = false;
      },
      complete: () => { this.cargando = false; }
    });
  }

  eliminar(proceso: Proceso): void {
    const aviso = proceso.nivel === 1
      ? `¿Eliminar el Proceso N1 "${proceso.nombre}"? Se eliminarán sus N2 asociados.`
      : `¿Eliminar el Proceso N2 "${proceso.nombre}"?`;

    this.confirmationService.confirm({
      message: aviso,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.cargando = true;
        this.procesoService.delete(proceso.id!).subscribe({
          next: () => { this.mostrarExito('Proceso eliminado exitosamente'); this.cargarProcesos(); },
          error: (err) => {
            const msg = err?.error?.message || 'No se puede eliminar. Verifique dependencias.';
            this.mostrarError(msg);
            this.cargando = false;
          },
          complete: () => { this.cargando = false; }
        });
      }
    });
  }

  cancelar(): void {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.procesoSeleccionado = null;
    this.procesosN1Options = [];
    this.formulario = this.inicializarFormulario();
  }

  private inicializarFormulario(): ProcesoRequest {
    return {
      macroprocesoId: 0,
      nivel: 1,
      procesoPadreId: undefined,
      nombre: '',
      descripcion: '',
      objetivos: '',
      estadoDocumentacion: EstadoDocumentacion.NO_DOCUMENTADO
    };
  }

  // ── Utilidades ────────────────────────────────────────────────────────────

  getNivelLabel(nivel: number): string {
    return nivel === 1 ? 'N1' : 'N2';
  }

  getNivelSeverity(nivel: number): 'info' | 'warning' {
    return nivel === 1 ? 'info' : 'warning';
  }

  /** Para la columna "N° Hijos": muestra hijos N2 para N1, o subprocesos para N2 */
  getCantidadHijos(proc: Proceso): number {
    return proc.nivel === 1 ? (proc.cantidadProcesosHijos ?? 0) : (proc.cantidadSubprocesos ?? 0);
  }

  getEstadoSeverity(estado: EstadoDocumentacion): 'success' | 'secondary' | 'info' | 'warning' | 'danger' | 'contrast' | undefined {
    const map: { [key in EstadoDocumentacion]: 'success' | 'secondary' | 'info' | 'warning' | 'danger' | 'contrast' } = {
      [EstadoDocumentacion.NO_DOCUMENTADO]:  'secondary',
      [EstadoDocumentacion.LEVANTAMIENTO]:   'info',
      [EstadoDocumentacion.FLUJODIAGRAMACION]:'info',
      [EstadoDocumentacion.CARACTERIZACION]: 'warning',
      [EstadoDocumentacion.VALIDACION]:      'warning',
      [EstadoDocumentacion.LEGALIZADO]:      'success',
      [EstadoDocumentacion.DIFUNDIDO]:       'success',
      [EstadoDocumentacion.MEJORA]:          'contrast'
    };
    return map[estado];
  }

  getEstadoNombre(estado: EstadoDocumentacion): string {
    const map: { [key in EstadoDocumentacion]: string } = {
      [EstadoDocumentacion.NO_DOCUMENTADO]:  'No Documentado',
      [EstadoDocumentacion.LEVANTAMIENTO]:   'Levantamiento',
      [EstadoDocumentacion.FLUJODIAGRAMACION]:'Flujodiagramación',
      [EstadoDocumentacion.CARACTERIZACION]: 'Caracterización',
      [EstadoDocumentacion.VALIDACION]:      'Validación',
      [EstadoDocumentacion.LEGALIZADO]:      'Legalizado',
      [EstadoDocumentacion.DIFUNDIDO]:       'Difundido',
      [EstadoDocumentacion.MEJORA]:          'Mejora Continua'
    };
    return map[estado];
  }

  isColumnVisible(field: string): boolean {
    return this.selectedColumns.some(col => col.field === field);
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
