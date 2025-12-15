import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProcesoService } from '../../services/proceso.service';
import { MacroprocesoService } from '../../services/macroproceso.service';
import {
    Proceso,
    ProcesoRequest,
    Macroproceso,
    EstadoDocumentacion
} from '../../models/inventario.model';

@Component({
    selector: 'app-procesos-list',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './procesos-list.component.html',
    styleUrls: ['./procesos-list.component.css']
})
export class ProcesosListComponent implements OnInit {
    procesos: Proceso[] = [];
    macroprocesos: Macroproceso[] = [];
    procesoSeleccionado: Proceso | null = null;
    modoEdicion = false;
    mostrarFormulario = false;
    filtroMacroproceso: number | null = null;

    // Para el formulario
    formulario: ProcesoRequest = this.inicializarFormulario();

    // Enumeraciones para templates
    estadosDocumentacion = Object.values(EstadoDocumentacion);

    constructor(
        private procesoService: ProcesoService,
        private macroprocesoService: MacroprocesoService
    ) { }

    ngOnInit(): void {
        this.cargarMacroprocesos();
        this.cargarProcesos();
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

    cargarProcesos(): void {
        if (this.filtroMacroproceso) {
            this.procesoService.getByMacroproceso(this.filtroMacroproceso).subscribe({
                next: (data) => {
                    this.procesos = data;
                },
                error: (error) => {
                    console.error('Error cargando procesos filtrados:', error);
                    alert('Error al cargar los procesos');
                }
            });
        } else {
            this.procesoService.getAll().subscribe({
                next: (data) => {
                    this.procesos = data;
                },
                error: (error) => {
                    console.error('Error cargando procesos:', error);
                    alert('Error al cargar los procesos');
                }
            });
        }
    }

    aplicarFiltro(): void {
        this.cargarProcesos();
    }

    limpiarFiltro(): void {
        this.filtroMacroproceso = null;
        this.cargarProcesos();
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
            objetivos: proceso.objetivos,
            estadoDocumentacion: proceso.estadoDocumentacion
        };
    }

    guardar(): void {
        if (!this.formulario.macroprocesoId) {
            alert('Debe seleccionar un macroproceso');
            return;
        }

        if (this.modoEdicion && this.procesoSeleccionado) {
            this.procesoService.update(this.procesoSeleccionado.id!, this.formulario).subscribe({
                next: () => {
                    alert('Proceso actualizado exitosamente');
                    this.cargarProcesos();
                    this.cancelar();
                },
                error: (error) => {
                    console.error('Error actualizando proceso:', error);
                    alert('Error al actualizar el proceso');
                }
            });
        } else {
            this.procesoService.create(this.formulario).subscribe({
                next: () => {
                    alert('Proceso creado exitosamente');
                    this.cargarProcesos();
                    this.cancelar();
                },
                error: (error) => {
                    console.error('Error creando proceso:', error);
                    alert('Error al crear el proceso');
                }
            });
        }
    }

    eliminar(proceso: Proceso): void {
        if (confirm(`¿Está seguro de eliminar el proceso "${proceso.nombre}"?`)) {
            this.procesoService.delete(proceso.id!).subscribe({
                next: () => {
                    alert('Proceso eliminado exitosamente');
                    this.cargarProcesos();
                },
                error: (error) => {
                    console.error('Error eliminando proceso:', error);
                    alert('Error al eliminar el proceso. Verifique que no tenga subprocesos asociados.');
                }
            });
        }
    }

    actualizarEstado(proceso: Proceso, nuevoEstado: EstadoDocumentacion): void {
        this.procesoService.updateEstado(proceso.id!, nuevoEstado).subscribe({
            next: () => {
                alert('Estado actualizado exitosamente');
                this.cargarProcesos();
            },
            error: (error) => {
                console.error('Error actualizando estado:', error);
                alert('Error al actualizar el estado');
            }
        });
    }

    verDetalle(proceso: Proceso): void {
        this.procesoService.getById(proceso.id!).subscribe({
            next: (data) => {
                console.log('Detalle del proceso con subprocesos:', data);
                // Aquí puedes mostrar un modal o navegar a una vista de detalle
                alert(`Proceso: ${data.nombre}\nSubprocesos: ${data.cantidadSubprocesos}\nAvance: ${data.porcentajeAvance}%`);
            },
            error: (error) => {
                console.error('Error obteniendo detalle:', error);
                alert('Error al obtener el detalle del proceso');
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
            objetivos: '',
            estadoDocumentacion: EstadoDocumentacion.NO_DOCUMENTADO
        };
    }

    getMacroprocesoNombre(id: number): string {
        const mp = this.macroprocesos.find(m => m.id === id);
        return mp ? mp.nombre : 'N/A';
    }

    getEstadoClass(estado: EstadoDocumentacion): string {
        const classMap: { [key in EstadoDocumentacion]: string } = {
            [EstadoDocumentacion.NO_DOCUMENTADO]: 'bg-secondary',
            [EstadoDocumentacion.LEVANTAMIENTO]: 'bg-info',
            [EstadoDocumentacion.FLUJODIAGRAMACION]: 'bg-primary',
            [EstadoDocumentacion.CARACTERIZACION]: 'bg-warning',
            [EstadoDocumentacion.VALIDACION]: 'bg-warning',
            [EstadoDocumentacion.LEGALIZADO]: 'bg-success',
            [EstadoDocumentacion.DIFUNDIDO]: 'bg-success',
            [EstadoDocumentacion.MEJORA]: 'bg-success'
        };
        return classMap[estado];
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
}