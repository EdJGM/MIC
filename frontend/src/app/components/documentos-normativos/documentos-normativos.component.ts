import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

// Wrappers
import {
  ButtonPrimaryComponent,
  ButtonCancelComponent,
  InputTextComponent,
  DropdownComponent,
  CalendarComponent,
  TextareaComponent,
  LoadingSpinnerComponent
} from '../../shared';

// Servicios
import { InformacionDocumentadaService } from '../../services/informacion-documentada.service';
import { MacroprocesoService } from '../../services/macroproceso.service';
import { ProcesoService } from '../../services/proceso.service';
import { SubprocesoService } from '../../services/subproceso.service';

// Modelos
import {
  InformacionDocumentadaRequest,
  SEDES,
  TIPOS_DOCUMENTO,
  MOTIVOS
} from '../../models/informacion-documentada.model';
import { Macroproceso, Proceso, Subproceso } from '../../models/inventario.model';

@Component({
  selector: 'app-documentos-normativos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ToastModule,
    ButtonPrimaryComponent,
    ButtonCancelComponent,
    InputTextComponent,
    DropdownComponent,
    CalendarComponent,
    TextareaComponent,
    LoadingSpinnerComponent
  ],
  templateUrl: './documentos-normativos.component.html',
  styleUrl: './documentos-normativos.component.css',
  providers: [MessageService]
})
export class DocumentosNormativosComponent implements OnInit {
  cargando: boolean = false;
  formulario: InformacionDocumentadaRequest = this.inicializarFormulario();

  // Opciones dropdowns
  sedes = SEDES;
  tiposDocumento = TIPOS_DOCUMENTO;
  motivos = MOTIVOS;

  // Datos para dropdowns dinámicos
  macroprocesosList: any[] = [];
  procesosN1List: any[] = [];
  procesosN2List: any[] = [];
  subprocesosN1List: any[] = [];
  subprocesosN2List: any[] = [];

  // Usuarios y Unidades (hardcodeados por ahora)
  usuariosList: any[] = [
    { label: 'ABRIL PORRAS VICTOR HUGO', value: 'ABRIL PORRAS VICTOR HUGO' },
    { label: 'BENITEZ GAICEDO BLANCA', value: 'BENITEZ GAICEDO BLANCA' },
    { label: 'NACATA LOACHAMIN NATHALY', value: 'NACATA LOACHAMIN NATHALY' }
  ];

  unidadesList: any[] = [
    { label: 'UPDI', value: 'UPDI' },
    { label: 'UAFA', value: 'UAFA' },
    { label: 'UTIC', value: 'UTIC' },
    { label: 'VDC', value: 'VDC' },
    { label: 'VAD', value: 'VAD' }
  ];

  constructor(
    private informacionDocumentadaService: InformacionDocumentadaService,
    private macroprocesoService: MacroprocesoService,
    private procesoService: ProcesoService,
    private subprocesoService: SubprocesoService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.cargarMacroprocesos();
  }

  cargarMacroprocesos(): void {
    this.macroprocesoService.getAll().subscribe({
      next: (data: Macroproceso[]) => {
        this.macroprocesosList = data.map((m: Macroproceso) => ({
          label: m.nombre,
          value: m.id
        }));
      },
      error: (error: any) => {
        console.error('Error al cargar macroprocesos:', error);
      }
    });
  }

  onMacroprocesoChange(event: any): void {
    const macroprocesoId = event.value;
    this.formulario.procesoN1Id = undefined;
    this.formulario.procesoN2Id = undefined;
    this.formulario.subprocesoN1Id = undefined;
    this.formulario.subprocesoN2Id = undefined;
    this.procesosN1List = [];
    this.procesosN2List = [];
    this.subprocesosN1List = [];
    this.subprocesosN2List = [];

    if (macroprocesoId) {
      this.cargarProcesosN1(macroprocesoId);
    }
  }

  cargarProcesosN1(macroprocesoId: number): void {
    this.procesoService.getByMacroproceso(macroprocesoId).subscribe({
      next: (data: Proceso[]) => {
        this.procesosN1List = data.map((p: Proceso) => ({
          label: p.nombre,
          value: p.id
        }));
      },
      error: (error: any) => {
        console.error('Error al cargar procesos N1:', error);
      }
    });
  }

  onProcesoN1Change(event: any): void {
    const procesoId = event.value;
    this.formulario.subprocesoN1Id = undefined;
    this.formulario.subprocesoN2Id = undefined;
    this.subprocesosN1List = [];
    this.subprocesosN2List = [];

    if (procesoId) {
      this.cargarSubprocesosN1(procesoId);
    }
  }

  cargarSubprocesosN1(procesoId: number): void {
    this.subprocesoService.getByProceso(procesoId).subscribe({
      next: (data: Subproceso[]) => {
        this.subprocesosN1List = data.map((s: Subproceso) => ({
          label: s.nombre,
          value: s.id
        }));
      },
      error: (error: any) => {
        console.error('Error al cargar subprocesos N1:', error);
      }
    });
  }

  guardar(): void {
    if (!this.validarFormulario()) {
      return;
    }

    this.cargando = true;
    this.informacionDocumentadaService.crear(this.formulario).subscribe({
      next: () => {
        this.mostrarExito('Documento creado correctamente');
        this.limpiarFormulario();
        this.cargando = false;
      },
      error: (error: any) => {
        console.error('Error al crear:', error);
        this.mostrarError('No se pudo crear el documento');
        this.cargando = false;
      }
    });
  }

  validarFormulario(): boolean {
    if (!this.formulario.unidad || !this.formulario.solicitadoPor ||
      !this.formulario.sede || !this.formulario.tipoDocumento ||
      !this.formulario.nombreDocumento || !this.formulario.motivo) {
      this.mostrarAdvertencia('Por favor complete todos los campos requeridos (*)');
      return false;
    }
    return true;
  }

  limpiarFormulario(): void {
    this.formulario = this.inicializarFormulario();
    this.procesosN1List = [];
    this.procesosN2List = [];
    this.subprocesosN1List = [];
    this.subprocesosN2List = [];
  }

  cancelar(): void {
    this.limpiarFormulario();
  }

  inicializarFormulario(): InformacionDocumentadaRequest {
    return {
      fechaSolicitud: new Date().toISOString().split('T')[0],
      unidad: '',
      solicitadoPor: '',
      sede: '',
      tipoDocumento: '',
      nombreDocumento: '',
      motivo: '',
      mes: new Date().getMonth() + 1,
      anio: new Date().getFullYear(),
      version: 'V1'
    };
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

  mostrarAdvertencia(mensaje: string): void {
    this.messageService.add({
      severity: 'warn',
      summary: 'Advertencia',
      detail: mensaje
    });
  }
}