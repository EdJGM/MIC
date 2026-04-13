import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ParametroSistemaService } from '../../services/parametro-sistema.service';
import { ParametroSistema, ParametroSistemaRequest } from '../../models/configuracion.model';

import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';

import {
  ButtonPrimaryComponent,
  ButtonCancelComponent,
  ButtonAddComponent,
  InputTextComponent,
  PanelComponent,
  DataTableComponent
} from '../../shared';

@Component({
  selector: 'app-configuracion-parametros',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CardModule,
    TagModule,
    ButtonPrimaryComponent,
    ButtonCancelComponent,
    ButtonAddComponent,
    InputTextComponent,
    PanelComponent,
    DataTableComponent
  ],
  templateUrl: './configuracion-parametros.component.html',
  styleUrls: ['./configuracion-parametros.component.css']
})
export class ConfiguracionParametrosComponent implements OnInit {
  parametros: ParametroSistema[] = [];
  parametroSeleccionado: ParametroSistema | null = null;
  mostrarFormulario = false;

  formulario: ParametroSistemaRequest = { valor: '' };

  cols = [
    { field: 'clave',       header: 'Clave' },
    { field: 'valor',       header: 'Valor' },
    { field: 'tipo',        header: 'Tipo' },
    { field: 'descripcion', header: 'Descripción' }
  ];

  constructor(private parametroService: ParametroSistemaService) {}

  ngOnInit(): void {
    this.cargarParametros();
  }

  cargarParametros(): void {
    this.parametroService.getAll().subscribe({
      next: (data) => {
        this.parametros = data;
      },
      error: (error) => {
        console.error('Error cargando parámetros:', error);
        alert('Error al cargar los parámetros del sistema');
      }
    });
  }

  editarParametro(parametro: ParametroSistema): void {
    this.parametroSeleccionado = parametro;
    this.mostrarFormulario = true;
    this.formulario = { valor: parametro.valor };
  }

  guardar(): void {
    if (!this.parametroSeleccionado) return;
    this.parametroService.update(this.parametroSeleccionado.clave, this.formulario).subscribe({
      next: () => {
        alert('Parámetro actualizado exitosamente');
        this.cargarParametros();
        this.cancelar();
      },
      error: (error) => {
        console.error('Error actualizando parámetro:', error);
        alert('Error al actualizar el parámetro');
      }
    });
  }

  cancelar(): void {
    this.mostrarFormulario = false;
    this.parametroSeleccionado = null;
    this.formulario = { valor: '' };
  }

  getTipoSeverity(tipo: string): 'success' | 'info' | 'warning' {
    const map: Record<string, 'success' | 'info' | 'warning'> = {
      NUMBER: 'info',
      TEXT: 'success',
      BOOLEAN: 'warning'
    };
    return map[tipo] ?? 'info';
  }
}
