import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificacionService } from '../../services/notificacion.service';
import { Notificacion, TipoNotificacion } from '../../models/configuracion.model';

import { TagModule } from 'primeng/tag';

import {
  ButtonPrimaryComponent,
  ButtonSecondaryComponent,
  ButtonAddComponent,
  PanelComponent,
  DataTableComponent
} from '../../shared';

// ID del usuario en sesión (temporal hasta integrar token universitario)
const USUARIO_SESION = 'admin-espe-001';

@Component({
  selector: 'app-notificaciones',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TagModule,
    ButtonPrimaryComponent,
    ButtonSecondaryComponent,
    ButtonAddComponent,
    PanelComponent,
    DataTableComponent
  ],
  templateUrl: './notificaciones.component.html',
  styleUrls: ['./notificaciones.component.css']
})
export class NotificacionesComponent implements OnInit {
  notificaciones: Notificacion[] = [];
  soloNoLeidas = false;
  cantidadNoLeidas = 0;

  cols = [
    { field: 'tipo',          header: 'Tipo' },
    { field: 'titulo',        header: 'Título' },
    { field: 'mensaje',       header: 'Mensaje' },
    { field: 'modulo',        header: 'Módulo' },
    { field: 'fechaCreacion', header: 'Fecha' }
  ];

  constructor(private notificacionService: NotificacionService) {}

  ngOnInit(): void {
    this.cargarNotificaciones();
    this.actualizarContador();
  }

  cargarNotificaciones(): void {
    const observable = this.soloNoLeidas
      ? this.notificacionService.getNoLeidas(USUARIO_SESION)
      : this.notificacionService.getPorUsuario(USUARIO_SESION);

    observable.subscribe({
      next: (data) => {
        this.notificaciones = data;
      },
      error: (error) => {
        console.error('Error cargando notificaciones:', error);
        alert('Error al cargar las notificaciones');
      }
    });
  }

  actualizarContador(): void {
    this.notificacionService.contarNoLeidas(USUARIO_SESION).subscribe({
      next: (res) => {
        this.cantidadNoLeidas = res.noLeidas;
      },
      error: (error) => {
        console.error('Error contando notificaciones:', error);
      }
    });
  }

  marcarLeida(notificacion: Notificacion): void {
    if (notificacion.leida) return;
    this.notificacionService.marcarComoLeida(notificacion.id!).subscribe({
      next: () => {
        this.cargarNotificaciones();
        this.actualizarContador();
      },
      error: (error) => {
        console.error('Error marcando notificación como leída:', error);
        alert('Error al marcar la notificación');
      }
    });
  }

  marcarTodasLeidas(): void {
    this.notificacionService.marcarTodasComoLeidas(USUARIO_SESION).subscribe({
      next: () => {
        this.cargarNotificaciones();
        this.actualizarContador();
      },
      error: (error) => {
        console.error('Error marcando todas como leídas:', error);
        alert('Error al marcar las notificaciones');
      }
    });
  }

  toggleFiltro(): void {
    this.soloNoLeidas = !this.soloNoLeidas;
    this.cargarNotificaciones();
  }

  getTipoSeverity(tipo: TipoNotificacion | undefined): 'success' | 'info' | 'warning' | 'danger' {
    const map: Record<TipoNotificacion, 'success' | 'info' | 'warning' | 'danger'> = {
      [TipoNotificacion.VENCIMIENTO]: 'danger',
      [TipoNotificacion.CAMBIO_ESTADO]: 'info',
      [TipoNotificacion.PUBLICACION]: 'success',
      [TipoNotificacion.ASIGNACION]: 'warning',
      [TipoNotificacion.PROCESO_PENDIENTE]: 'warning'
    };
    return tipo ? (map[tipo] ?? 'info') : 'info';
  }
}
