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
  estadosOptions:        { label: string; value: string }[] = [];
  procesosN2Options:     { label: string; value: number }[] = [];
  subprocesosN1Options:  { label: string; value: number }[] = [];
  nivelesOptions:        { label: string; value: number }[] = [
    { label: 'SP-N1 — Subproceso de primer nivel', value: 1 },
    { label: 'SP-N2 — Subproceso de segundo nivel (hijo de SP-N1)', value: 2 }
  ];

  // Column toggle
  cols: Column[] = [
    { field: 'nivel',                 header: 'Nivel',           sortable: true },
    { field: 'codigo',                header: 'Código',          sortable: true },
    { field: 'nombre',                header: 'Nombre',          sortable: true },
    { field: 'procesoNombre',         header: 'Proceso (N2)',    sortable: true },
    { field: 'subprocesoPadreNombre', header: 'SP Padre (N1)',   sortable: true },
    { field: 'estadoDocumentacion',   header: 'Estado',          sortable: true },
    { field: 'porcentajeAvance',      header: 'Avance %',        sortable: true },
    { field: 'cantidadHijos',         header: 'N° Hijos (SP-N2)', sortable: false }
  ];
  selectedColumns: Column[] = this.cols.filter(c =>
    ['nivel','codigo','nombre','procesoNombre','estadoDocumentacion','porcentajeAvance','cantidadHijos'].includes(c.field)
  );

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
      this.cargarProcesosN2()
    ]).finally(() => { this.cargando = false; });
  }

  cargarSubprocesos(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.subprocesoService.getAll().subscribe({
        next: (data) => { this.subprocesos = data; resolve(); },
        error: (err) => { this.mostrarError('Error al cargar los subprocesos'); reject(err); }
      });
    });
  }

  cargarProcesosN2(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.procesoService.getAll().subscribe({
        next: (data) => {
          this.procesos = data;
          const n2 = data.filter(p => p.nivel === 2);
          this.procesosN2Options = [
            { label: 'Seleccione un proceso N2', value: 0 },
            ...n2.map(p => ({ label: `${p.codigo} - ${p.nombre}`, value: p.id! }))
          ];
          resolve();
        },
        error: (err) => { this.mostrarError('Error al cargar los procesos'); reject(err); }
      });
    });
  }

  /** Cuando cambia el Proceso N2: recarga SP-N1 si estamos en nivel 2 */
  onProcesoN2Change(procesoId: number): void {
    this.formulario.subprocesoPadreId = undefined;
    this.subprocesosN1Options = [];
    if (this.formulario.nivel === 2 && procesoId && procesoId !== 0) {
      this.cargarSubprocesosN1(procesoId);
    }
  }

  /** Cuando cambia el nivel en el formulario */
  onNivelChange(nivel: number): void {
    this.formulario.subprocesoPadreId = undefined;
    this.subprocesosN1Options = [];
    if (nivel === 2 && this.formulario.procesoId && this.formulario.procesoId !== 0) {
      this.cargarSubprocesosN1(this.formulario.procesoId);
    }
  }

  private cargarSubprocesosN1(procesoId: number): void {
    this.subprocesoService.getByProcesoYNivel(procesoId, 1).subscribe({
      next: (data) => {
        this.subprocesosN1Options = [
          { label: 'Seleccione subproceso N1 padre', value: 0 },
          ...data.map(sp => ({ label: `${sp.codigo} - ${sp.nombre}`, value: sp.id! }))
        ];
      },
      error: () => this.mostrarError('Error al cargar los subprocesos N1')
    });
  }

  nuevoSubproceso(): void {
    this.modoEdicion = false;
    this.mostrarFormulario = true;
    this.formulario = this.inicializarFormulario();
    this.subprocesosN1Options = [];
    this.subprocesoSeleccionado = null;
  }

  editarSubproceso(subproceso: Subproceso): void {
    this.modoEdicion = true;
    this.mostrarFormulario = true;
    this.subprocesoSeleccionado = subproceso;
    this.formulario = {
      procesoId: subproceso.procesoId,
      nivel: subproceso.nivel,
      subprocesoPadreId: subproceso.subprocesoPadreId,
      nombre: subproceso.nombre,
      descripcion: subproceso.descripcion,
      estadoDocumentacion: subproceso.estadoDocumentacion
    };
    // Si es SP-N2, precargar opciones de subproceso padre
    if (subproceso.nivel === 2 && subproceso.procesoId) {
      this.cargarSubprocesosN1(subproceso.procesoId);
    }
  }

  guardar(): void {
    if (!this.formulario.procesoId || this.formulario.procesoId === 0) {
      this.mostrarAdvertencia('Debe seleccionar un Proceso N2');
      return;
    }
    if (this.formulario.nivel === 2 && (!this.formulario.subprocesoPadreId || this.formulario.subprocesoPadreId === 0)) {
      this.mostrarAdvertencia('Debe seleccionar el Subproceso N1 padre para un SP-N2');
      return;
    }

    this.cargando = true;
    const op$ = this.modoEdicion && this.subprocesoSeleccionado
      ? this.subprocesoService.update(this.subprocesoSeleccionado.id!, this.formulario)
      : this.subprocesoService.create(this.formulario);

    op$.subscribe({
      next: () => {
        this.mostrarExito(this.modoEdicion ? 'Subproceso actualizado exitosamente' : 'Subproceso creado exitosamente');
        this.cargarSubprocesos();
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

  eliminar(subproceso: Subproceso): void {
    const aviso = subproceso.nivel === 1
      ? `¿Eliminar el Subproceso SP-N1 "${subproceso.nombre}"? Se eliminarán sus SP-N2 asociados.`
      : `¿Eliminar el Subproceso SP-N2 "${subproceso.nombre}"?`;

    this.confirmationService.confirm({
      message: aviso,
      header: 'Confirmar Eliminación',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sí, eliminar',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.cargando = true;
        this.subprocesoService.delete(subproceso.id!).subscribe({
          next: () => { this.mostrarExito('Subproceso eliminado exitosamente'); this.cargarSubprocesos(); },
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
    this.subprocesoSeleccionado = null;
    this.subprocesosN1Options = [];
    this.formulario = this.inicializarFormulario();
  }

  private inicializarFormulario(): SubprocesoRequest {
    return {
      procesoId: 0,
      nivel: 1,
      subprocesoPadreId: undefined,
      nombre: '',
      descripcion: '',
      estadoDocumentacion: EstadoDocumentacion.NO_DOCUMENTADO
    };
  }

  // ── Utilidades ────────────────────────────────────────────────────────────

  getNivelLabel(nivel: number): string {
    return nivel === 1 ? 'SP-N1' : 'SP-N2';
  }

  getNivelSeverity(nivel: number): 'info' | 'warning' {
    return nivel === 1 ? 'info' : 'warning';
  }

  /** Para la columna "N° Hijos": SP-N1 muestra sus SP-N2; SP-N2 siempre 0 */
  getCantidadHijos(sp: Subproceso): number {
    return sp.nivel === 1 ? (sp.cantidadSubprocesosHijos ?? 0) : 0;
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
