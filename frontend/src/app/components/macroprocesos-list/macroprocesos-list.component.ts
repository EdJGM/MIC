import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MacroprocesoService } from '../../services/macroproceso.service';
import {
  Macroproceso,
  MacroprocesoRequest,
  TipoMacroproceso,
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
  selector: 'app-macroprocesos-list',
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
  templateUrl: './macroprocesos-list.component.html',
  styleUrls: ['./macroprocesos-list.component.css']
})
export class MacroprocesosListComponent implements OnInit {
  macroprocesos: Macroproceso[] = [];
  macroprocesoSeleccionado: Macroproceso | null = null;
  modoEdicion = false;
  mostrarFormulario = false;

  // Para el formulario
  formulario: MacroprocesoRequest = this.inicializarFormulario();

  // Enumeraciones para templates
  tiposMacroproceso = Object.values(TipoMacroproceso);
  estadosDocumentacion = Object.values(EstadoDocumentacion);

  // Column toggle
  cols: Column[] = [];
  selectedColumns: Column[] = [];

  constructor(private macroprocesoService: MacroprocesoService) {}

  ngOnInit(): void {
    this.cargarMacroprocesos();
    this.initColumns();
  }

  initColumns(): void {
    this.cols = [
      { field: 'codigo', header: 'Código' },
      { field: 'tipo', header: 'Tipo' },
      { field: 'nombre', header: 'Nombre' },
      { field: 'unidadEstrategica', header: 'Unidad Estratégica' },
      { field: 'responsablePrincipal', header: 'Responsable' },
      { field: 'estadoDocumentacion', header: 'Estado' },
      { field: 'porcentajeAvance', header: 'Avance' },
      { field: 'cantidadProcesos', header: 'Procesos' }
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
        alert('Error al cargar los macroprocesos');
      }
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
    if (this.modoEdicion && this.macroprocesoSeleccionado) {
      this.macroprocesoService.update(this.macroprocesoSeleccionado.id!, this.formulario).subscribe({
        next: () => {
          alert('Macroproceso actualizado exitosamente');
          this.cargarMacroprocesos();
          this.cancelar();
        },
        error: (error) => {
          console.error('Error actualizando macroproceso:', error);
          alert('Error al actualizar el macroproceso');
        }
      });
    } else {
      this.macroprocesoService.create(this.formulario).subscribe({
        next: () => {
          alert('Macroproceso creado exitosamente');
          this.cargarMacroprocesos();
          this.cancelar();
        },
        error: (error) => {
          console.error('Error creando macroproceso:', error);
          alert('Error al crear el macroproceso');
        }
      });
    }
  }

  eliminar(macroproceso: Macroproceso): void {
    if (confirm(`¿Está seguro de eliminar el macroproceso "${macroproceso.nombre}"?`)) {
      this.macroprocesoService.delete(macroproceso.id!).subscribe({
        next: () => {
          alert('Macroproceso eliminado exitosamente');
          this.cargarMacroprocesos();
        },
        error: (error) => {
          console.error('Error eliminando macroproceso:', error);
          alert('Error al eliminar el macroproceso. Verifique que no tenga procesos asociados.');
        }
      });
    }
  }

  actualizarEstado(macroproceso: Macroproceso, nuevoEstado: EstadoDocumentacion): void {
    this.macroprocesoService.updateEstado(macroproceso.id!, nuevoEstado).subscribe({
      next: () => {
        alert('Estado actualizado exitosamente');
        this.cargarMacroprocesos();
      },
      error: (error) => {
        console.error('Error actualizando estado:', error);
        alert('Error al actualizar el estado');
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

  getProgressBarColor(porcentaje: number): string {
    if (porcentaje === 0) return '#6c757d';
    if (porcentaje < 60) return '#17a2b8';
    if (porcentaje < 75) return '#ffc107';
    if (porcentaje < 90) return '#007bff';
    return '#28a745';
  }

  isColumnVisible(field: string): boolean {
    return this.selectedColumns.some(col => col.field === field);
  }
}
