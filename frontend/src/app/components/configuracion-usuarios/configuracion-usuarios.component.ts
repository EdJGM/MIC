import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UsuarioLocalService } from '../../services/usuario-local.service';
import { UsuarioLocal, UsuarioLocalRequest, RolLocal, ROL_LOCAL_OPTIONS } from '../../models/configuracion.model';

import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';

import {
  ButtonPrimaryComponent,
  ButtonCancelComponent,
  ButtonAddComponent,
  InputTextComponent,
  DropdownComponent,
  PanelComponent,
  DataTableComponent
} from '../../shared';

@Component({
  selector: 'app-configuracion-usuarios',
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
    DropdownComponent,
    PanelComponent,
    DataTableComponent
  ],
  templateUrl: './configuracion-usuarios.component.html',
  styleUrls: ['./configuracion-usuarios.component.css']
})
export class ConfiguracionUsuariosComponent implements OnInit {
  usuarios: UsuarioLocal[] = [];
  usuarioSeleccionado: UsuarioLocal | null = null;
  modoEdicion = false;
  mostrarFormulario = false;

  rolesOpciones = ROL_LOCAL_OPTIONS;

  formulario: UsuarioLocalRequest = this.inicializarFormulario();

  cols = [
    { field: 'nombre',         header: 'Nombre' },
    { field: 'email',          header: 'Correo' },
    { field: 'externalId',     header: 'ID Externo' },
    { field: 'rolNombre',      header: 'Rol' },
    { field: 'unidadAsignada', header: 'Unidad' },
    { field: 'activo',         header: 'Estado' }
  ];

  constructor(private usuarioService: UsuarioLocalService) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.usuarioService.getAll().subscribe({
      next: (data) => {
        this.usuarios = data;
      },
      error: (error) => {
        console.error('Error cargando usuarios:', error);
        alert('Error al cargar los usuarios');
      }
    });
  }

  nuevoUsuario(): void {
    this.modoEdicion = false;
    this.mostrarFormulario = true;
    this.formulario = this.inicializarFormulario();
    this.usuarioSeleccionado = null;
  }

  editarUsuario(usuario: UsuarioLocal): void {
    this.modoEdicion = true;
    this.mostrarFormulario = true;
    this.usuarioSeleccionado = usuario;
    this.formulario = {
      externalId: usuario.externalId,
      nombre: usuario.nombre,
      email: usuario.email,
      telefono: usuario.telefono ?? '',
      rolLocal: usuario.rolLocal,
      unidadAsignada: usuario.unidadAsignada ?? ''
    };
  }

  guardar(): void {
    if (this.modoEdicion && this.usuarioSeleccionado) {
      this.usuarioService.update(this.usuarioSeleccionado.id!, this.formulario).subscribe({
        next: () => {
          alert('Usuario actualizado exitosamente');
          this.cargarUsuarios();
          this.cancelar();
        },
        error: (error) => {
          console.error('Error actualizando usuario:', error);
          alert('Error al actualizar el usuario');
        }
      });
    } else {
      this.usuarioService.create(this.formulario).subscribe({
        next: () => {
          alert('Usuario creado exitosamente');
          this.cargarUsuarios();
          this.cancelar();
        },
        error: (error) => {
          console.error('Error creando usuario:', error);
          alert('Error al crear el usuario');
        }
      });
    }
  }

  toggleActivo(usuario: UsuarioLocal): void {
    const operacion = usuario.activo
      ? this.usuarioService.desactivar(usuario.id!)
      : this.usuarioService.activar(usuario.id!);

    operacion.subscribe({
      next: () => {
        alert(`Usuario ${usuario.activo ? 'desactivado' : 'activado'} exitosamente`);
        this.cargarUsuarios();
      },
      error: (error) => {
        console.error('Error cambiando estado del usuario:', error);
        alert('Error al cambiar el estado del usuario');
      }
    });
  }

  cancelar(): void {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.usuarioSeleccionado = null;
    this.formulario = this.inicializarFormulario();
  }

  getRolSeverity(rol: RolLocal): 'success' | 'info' | 'warning' {
    const map: Record<RolLocal, 'success' | 'info' | 'warning'> = {
      [RolLocal.ADMINISTRADOR]: 'success',
      [RolLocal.USUARIO_OPERATIVO]: 'info',
      [RolLocal.UNIDAD]: 'warning'
    };
    return map[rol] ?? 'info';
  }

  private inicializarFormulario(): UsuarioLocalRequest {
    return {
      externalId: '',
      nombre: '',
      email: '',
      telefono: '',
      rolLocal: RolLocal.USUARIO_OPERATIVO,
      unidadAsignada: ''
    };
  }
}
