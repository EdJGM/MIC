export enum RolLocal {
  ADMINISTRADOR = 'ADMINISTRADOR',
  USUARIO_OPERATIVO = 'USUARIO_OPERATIVO',
  UNIDAD = 'UNIDAD'
}

export enum TipoParametro {
  NUMBER = 'NUMBER',
  TEXT = 'TEXT',
  BOOLEAN = 'BOOLEAN'
}

export enum TipoNotificacion {
  VENCIMIENTO = 'VENCIMIENTO',
  CAMBIO_ESTADO = 'CAMBIO_ESTADO',
  PUBLICACION = 'PUBLICACION',
  ASIGNACION = 'ASIGNACION',
  PROCESO_PENDIENTE = 'PROCESO_PENDIENTE'
}

export enum AccionAudit {
  CREATE = 'CREATE',
  UPDATE = 'UPDATE',
  DELETE = 'DELETE',
  READ = 'READ',
  ACTIVATE = 'ACTIVATE',
  DEACTIVATE = 'DEACTIVATE'
}

export interface UsuarioLocal {
  id?: number;
  externalId: string;
  nombre: string;
  email: string;
  telefono?: string;
  rolLocal: RolLocal;
  rolNombre?: string;
  unidadAsignada?: string;
  activo?: boolean;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  creadoPor?: string;
  actualizadoPor?: string;
}

export interface UsuarioLocalRequest {
  externalId: string;
  nombre: string;
  email: string;
  telefono?: string;
  rolLocal: RolLocal;
  unidadAsignada?: string;
}

export interface ParametroSistema {
  id?: number;
  clave: string;
  valor: string;
  descripcion?: string;
  tipo: TipoParametro;
  fechaActualizacion?: string;
  modificadoPor?: string;
}

export interface ParametroSistemaRequest {
  valor: string;
}

export interface AuditLog {
  id?: number;
  fechaHora?: string;
  usuarioId?: string;
  usuarioNombre?: string;
  accion?: AccionAudit;
  accionDescripcion?: string;
  modulo?: string;
  entidadId?: number;
  descripcion?: string;
  resultado?: string;
  ip?: string;
}

export interface Notificacion {
  id?: number;
  tipo?: TipoNotificacion;
  tipoNombre?: string;
  titulo?: string;
  mensaje?: string;
  destinatarioId?: string;
  leida?: boolean;
  fechaCreacion?: string;
  entidadId?: number;
  modulo?: string;
}

export const ROL_LOCAL_OPTIONS = [
  { label: 'Administrador', value: RolLocal.ADMINISTRADOR },
  { label: 'Usuario Operativo', value: RolLocal.USUARIO_OPERATIVO },
  { label: 'Unidad', value: RolLocal.UNIDAD }
];

export const ACCION_AUDIT_OPTIONS = [
  { label: 'Crear', value: AccionAudit.CREATE },
  { label: 'Actualizar', value: AccionAudit.UPDATE },
  { label: 'Eliminar', value: AccionAudit.DELETE },
  { label: 'Consultar', value: AccionAudit.READ },
  { label: 'Activar', value: AccionAudit.ACTIVATE },
  { label: 'Desactivar', value: AccionAudit.DEACTIVATE }
];
