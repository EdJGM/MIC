import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ObjetivoEspecificoService } from '../../services/objetivo-especifico.service';
import { ObjetivoEspecifico, ObjetivoEspecificoRequest } from '../../models/inventario.model';

// PrimeNG Imports (solo los que no tienen wrapper)
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';

// Shared Components (Wrappers)
import {
  ButtonPrimaryComponent,
  ButtonCancelComponent,
  ButtonAddComponent,
  InputTextComponent,
  TextareaComponent,
  PanelComponent,
  DataTableComponent
} from '../../shared';

@Component({
  selector: 'app-configuracion-objetivos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    // PrimeNG (sin wrapper)
    CardModule,
    TableModule,
    // Shared Wrappers
    ButtonPrimaryComponent,
    ButtonCancelComponent,
    ButtonAddComponent,
    InputTextComponent,
    TextareaComponent,
    PanelComponent,
    DataTableComponent
  ],
  templateUrl: './configuracion-objetivos.component.html',
  styleUrls: ['./configuracion-objetivos.component.css']
})
export class ConfiguracionObjetivosComponent implements OnInit {
  objetivos: ObjetivoEspecifico[] = [];
  objetivoSeleccionado: ObjetivoEspecifico | null = null;
  modoEdicion = false;
  mostrarFormulario = false;

  formulario: ObjetivoEspecificoRequest = this.inicializarFormulario();

  cols = [
    { field: 'nombre', header: 'Nombre' },
    { field: 'descripcion', header: 'Descripción' }
  ];

  constructor(private objetivoService: ObjetivoEspecificoService) {}

  ngOnInit(): void {
    this.cargarObjetivos();
  }

  cargarObjetivos(): void {
    this.objetivoService.getAll().subscribe({
      next: (data) => {
        this.objetivos = data;
      },
      error: (error) => {
        console.error('Error cargando objetivos especificos:', error);
        alert('Error al cargar los objetivos especificos');
      }
    });
  }

  nuevoObjetivo(): void {
    this.modoEdicion = false;
    this.mostrarFormulario = true;
    this.formulario = this.inicializarFormulario();
    this.objetivoSeleccionado = null;
  }

  editarObjetivo(objetivo: ObjetivoEspecifico): void {
    this.modoEdicion = true;
    this.mostrarFormulario = true;
    this.objetivoSeleccionado = objetivo;
    this.formulario = {
      nombre: objetivo.nombre,
      descripcion: objetivo.descripcion
    };
  }

  guardar(): void {
    if (this.modoEdicion && this.objetivoSeleccionado) {
      this.objetivoService.update(this.objetivoSeleccionado.id!, this.formulario).subscribe({
        next: () => {
          alert('Objetivo especifico actualizado exitosamente');
          this.cargarObjetivos();
          this.cancelar();
        },
        error: (error) => {
          console.error('Error actualizando objetivo especifico:', error);
          alert('Error al actualizar el objetivo especifico');
        }
      });
    } else {
      this.objetivoService.create(this.formulario).subscribe({
        next: () => {
          alert('Objetivo especifico creado exitosamente');
          this.cargarObjetivos();
          this.cancelar();
        },
        error: (error) => {
          console.error('Error creando objetivo especifico:', error);
          alert('Error al crear el objetivo especifico');
        }
      });
    }
  }

  eliminar(objetivo: ObjetivoEspecifico): void {
    if (confirm(`Esta seguro de eliminar el objetivo "${objetivo.nombre}"?`)) {
      this.objetivoService.delete(objetivo.id!).subscribe({
        next: () => {
          alert('Objetivo especifico eliminado exitosamente');
          this.cargarObjetivos();
        },
        error: (error) => {
          console.error('Error eliminando objetivo especifico:', error);
          alert('Error al eliminar el objetivo especifico');
        }
      });
    }
  }

  cancelar(): void {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.objetivoSeleccionado = null;
    this.formulario = this.inicializarFormulario();
  }

  private inicializarFormulario(): ObjetivoEspecificoRequest {
    return {
      nombre: '',
      descripcion: ''
    };
  }
}
