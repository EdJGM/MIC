import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SubprocesoService } from '../../services/subproceso.service';
import { ProcesoService } from '../../services/proceso.service';
import { MacroprocesoService } from '../../services/macroproceso.service';
import {
  Subproceso,
  SubprocesoRequest,
  Proceso,
  Macroproceso,
  EstadoDocumentacion
} from '../../models/inventario.model';

// PrimeNG Imports
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelectModule } from 'primeng/multiselect';
import { ProgressBarModule } from 'primeng/progressbar';
import { TagModule } from 'primeng/tag';
import { TooltipModule } from 'primeng/tooltip';
import { CardModule } from 'primeng/card';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { PanelModule } from 'primeng/panel';

interface Column {
  field: string;
  header: string;
}

@Component({
  selector: 'app-subprocesos-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    DropdownModule,
    MultiSelectModule,
    ProgressBarModule,
    TagModule,
    TooltipModule,
    CardModule,
    InputTextareaModule,
    PanelModule
  ],
  templateUrl: './subprocesos-list.component.html',
  styleUrls: ['./subprocesos-list.component.css']
})
export class SubprocesosListComponent implements OnInit {
  subprocesos: Subproceso[] = [];
  procesos: Proceso[] = [];
  macroprocesos: Macroproceso[] = [];
  subprocesoSeleccionado: Subproceso | null = null;
  modoEdicion = false;
  mostrarFormulario = false;

  // Filtros
  filtroMacroproceso: number | null = null;
  filtroProceso: number | null = null;
  procesosFiltrados: Proceso[] = [];

  // Para el formulario
  formulario: SubprocesoRequest = this.inicializarFormulario();
  procesoFormularioFiltrados: Proceso[] = [];
  formularioMacroprocesoId: number | null = null;

  // Enumeraciones para templates
  estadosDocumentacion = Object.values(EstadoDocumentacion);

  // Column toggle
  cols: Column[] = [];
  selectedColumns: Column[] = [];

  constructor(
    private subprocesoService: SubprocesoService,
    private procesoService: ProcesoService,
    private macroprocesoService: MacroprocesoService
  ) {}

  ngOnInit(): void {
    this.cargarMacroprocesos();
    this.cargarProcesos();
    this.cargarSubprocesos();
    this.initColumns();
  }

  initColumns(): void {
    this.cols = [
      { field: 'codigo', header: 'Código' },
      { field: 'nombre', header: 'Nombre' },
      { field: 'procesoNombre', header: 'Proceso' },
      { field: 'macroproceso', header: 'Macroproceso' },
      { field: 'descripcion', header: 'Descripción' },
      { field: 'estadoDocumentacion', header: 'Estado' },
      { field: 'porcentajeAvance', header: 'Avance' }
    ];
    this.selectedColumns = this.cols;
  }

  cargarMacroprocesos(): void {
    this.macroprocesoService.getAll().subscribe({
      next: (data) => {
        this.macroprocesos = data;
      },
      error: (error) => {
        console.error('Error cargando macroprocesos:', error);
      }
    });
  }

  cargarProcesos(): void {
    this.procesoService.getAll().subscribe({
      next: (data) => {
        this.procesos = data;
        this.procesoFormularioFiltrados = data;
      },
      error: (error) => {
        console.error('Error cargando procesos:', error);
        alert('Error al cargar los procesos');
      }
    });
  }

  cargarSubprocesos(): void {
    if (this.filtroProceso) {
      this.subprocesoService.getByProceso(this.filtroProceso).subscribe({
        next: (data) => {
          this.subprocesos = data;
        },
        error: (error) => {
          console.error('Error cargando subprocesos filtrados:', error);
          alert('Error al cargar los subprocesos');
        }
      });
    } else {
      this.subprocesoService.getAll().subscribe({
        next: (data) => {
          this.subprocesos = data;
        },
        error: (error) => {
          console.error('Error cargando subprocesos:', error);
          alert('Error al cargar los subprocesos');
        }
      });
    }
  }

  onFiltroMacroprocesoChange(): void {
    if (this.filtroMacroproceso) {
      this.procesosFiltrados = this.procesos.filter(
        p => p.macroprocesoId === this.filtroMacroproceso
      );
      this.filtroProceso = null;
    } else {
      this.procesosFiltrados = [];
      this.filtroProceso = null;
    }
  }

  aplicarFiltro(): void {
    this.cargarSubprocesos();
  }

  limpiarFiltro(): void {
    this.filtroMacroproceso = null;
    this.filtroProceso = null;
    this.procesosFiltrados = [];
    this.cargarSubprocesos();
  }

  onFormularioMacroprocesoChange(): void {
    if (this.formularioMacroprocesoId && this.formularioMacroprocesoId !== 0) {
      this.procesoFormularioFiltrados = this.procesos.filter(
        p => p.macroprocesoId === this.formularioMacroprocesoId
      );
      this.formulario.procesoId = 0;
    } else {
      this.procesoFormularioFiltrados = this.procesos;
    }
  }

  nuevoSubproceso(): void {
    this.modoEdicion = false;
    this.mostrarFormulario = true;
    this.formulario = this.inicializarFormulario();
    this.subprocesoSeleccionado = null;
    this.procesoFormularioFiltrados = this.procesos;
    this.formularioMacroprocesoId = null;
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

    // Filtrar procesos del macroproceso correspondiente
    const proceso = this.procesos.find(p => p.id === subproceso.procesoId);
    if (proceso) {
      this.formularioMacroprocesoId = proceso.macroprocesoId;
      this.procesoFormularioFiltrados = this.procesos.filter(
        p => p.macroprocesoId === proceso.macroprocesoId
      );
    }
  }

  guardar(): void {
    if (!this.formulario.procesoId || this.formulario.procesoId === 0) {
      alert('Debe seleccionar un proceso');
      return;
    }

    if (this.modoEdicion && this.subprocesoSeleccionado) {
      this.subprocesoService.update(this.subprocesoSeleccionado.id!, this.formulario).subscribe({
        next: () => {
          alert('Subproceso actualizado exitosamente');
          this.cargarSubprocesos();
          this.cancelar();
        },
        error: (error) => {
          console.error('Error actualizando subproceso:', error);
          alert('Error al actualizar el subproceso');
        }
      });
    } else {
      this.subprocesoService.create(this.formulario).subscribe({
        next: () => {
          alert('Subproceso creado exitosamente');
          this.cargarSubprocesos();
          this.cancelar();
        },
        error: (error) => {
          console.error('Error creando subproceso:', error);
          alert('Error al crear el subproceso');
        }
      });
    }
  }

  eliminar(subproceso: Subproceso): void {
    if (confirm(`¿Está seguro de eliminar el subproceso "${subproceso.nombre}"?`)) {
      this.subprocesoService.delete(subproceso.id!).subscribe({
        next: () => {
          alert('Subproceso eliminado exitosamente');
          this.cargarSubprocesos();
        },
        error: (error) => {
          console.error('Error eliminando subproceso:', error);
          alert('Error al eliminar el subproceso');
        }
      });
    }
  }

  actualizarEstado(subproceso: Subproceso, event: Event): void {
    const select = event.target as HTMLSelectElement;
    const nuevoEstado = select.value as EstadoDocumentacion;

    this.subprocesoService.updateEstado(subproceso.id!, nuevoEstado).subscribe({
      next: () => {
        alert('Estado actualizado exitosamente');
        this.cargarSubprocesos();
      },
      error: (error) => {
        console.error('Error actualizando estado:', error);
        alert('Error al actualizar el estado');
        select.value = subproceso.estadoDocumentacion;
      }
    });
  }

  cancelar(): void {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.subprocesoSeleccionado = null;
    this.formulario = this.inicializarFormulario();
    this.procesoFormularioFiltrados = this.procesos;
    this.formularioMacroprocesoId = null;
  }

  private inicializarFormulario(): SubprocesoRequest {
    return {
      procesoId: 0,
      nombre: '',
      descripcion: '',
      estadoDocumentacion: EstadoDocumentacion.NO_DOCUMENTADO
    };
  }

  getProcesoNombre(id: number): string {
    const p = this.procesos.find(proc => proc.id === id);
    return p ? p.nombre : 'N/A';
  }

  getMacroprocesoNombrePorProceso(procesoId: number): string {
    const proceso = this.procesos.find(p => p.id === procesoId);
    if (proceso) {
      const mp = this.macroprocesos.find(m => m.id === proceso.macroprocesoId);
      return mp ? mp.nombre : 'N/A';
    }
    return 'N/A';
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
      [EstadoDocumentacion.MEJORA]: 'success'
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
      [EstadoDocumentacion.MEJORA]: 'Mejora'
    };
    return nombreMap[estado];
  }

  getProgressBarClass(porcentaje: number): string {
    if (porcentaje === 0) return 'bg-secondary';
    if (porcentaje < 60) return 'bg-info';
    if (porcentaje < 75) return 'bg-warning';
    if (porcentaje < 90) return 'bg-primary';
    return 'bg-success';
  }

  isColumnVisible(field: string): boolean {
    return this.selectedColumns.some(col => col.field === field);
  }
}
