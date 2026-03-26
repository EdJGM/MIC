export enum TipoMacroproceso {
  REC = 'REC',
  UTIC = 'UTIC',
  USGN = 'USGN',
  VDC = 'VDC',
  VAD = 'VAD',
  VAG = 'VAG',
  VII = 'VII'
}

export enum EstadoDocumentacion {
  NO_DOCUMENTADO = 'NO_DOCUMENTADO',
  LEVANTAMIENTO = 'LEVANTAMIENTO',
  FLUJODIAGRAMACION = 'FLUJODIAGRAMACION',
  CARACTERIZACION = 'CARACTERIZACION',
  VALIDACION = 'VALIDACION',
  LEGALIZADO = 'LEGALIZADO',
  DIFUNDIDO = 'DIFUNDIDO',
  MEJORA = 'MEJORA'
}

export interface Macroproceso {
  id?: number;
  codigo: string;
  tipo: TipoMacroproceso;
  nombre: string;
  descripcion: string;
  unidadEstrategica: string;
  responsablePrincipal: string;
  objetivosEstrategicos: string;
  estadoDocumentacion: EstadoDocumentacion;
  porcentajeAvance: number;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  creadoPor?: string;
  actualizadoPor?: string;
  cantidadProcesos?: number;
  procesos?: Proceso[];
}

export interface Proceso {
  id?: number;
  codigo: string;
  nombre: string;
  descripcion: string;
  objetivos?: string;
  /** 1 = Proceso N1 | 2 = Proceso N2 */
  nivel: number;
  macroprocesoId: number;
  macroprocesoNombre?: string;
  procesoPadreId?: number;
  procesoPadreNombre?: string;
  estadoDocumentacion: EstadoDocumentacion;
  porcentajeAvance: number;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  creadoPor?: string;
  actualizadoPor?: string;
  /** Solo para N1: cantidad de Procesos N2 hijos */
  cantidadProcesosHijos?: number;
  /** Solo para N2: cantidad de Subprocesos N1 hijos */
  cantidadSubprocesos?: number;
  subprocesos?: Subproceso[];
}

export interface Subproceso {
  id?: number;
  codigo: string;
  nombre: string;
  descripcion: string;
  /** 1 = Subproceso N1 | 2 = Subproceso N2 */
  nivel: number;
  procesoId: number;
  procesoNombre?: string;
  subprocesoPadreId?: number;
  subprocesoPadreNombre?: string;
  estadoDocumentacion: EstadoDocumentacion;
  porcentajeAvance: number;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  creadoPor?: string;
  actualizadoPor?: string;
  /** Solo para SP-N1: cantidad de SP-N2 hijos */
  cantidadSubprocesosHijos?: number;
}

export interface MacroprocesoRequest {
  tipo: TipoMacroproceso;
  nombre: string;
  descripcion: string;
  unidadEstrategica: string;
  responsablePrincipal: string;
  objetivosEstrategicos: string;
  estadoDocumentacion?: EstadoDocumentacion;
}

export interface ProcesoRequest {
  macroprocesoId: number;
  /** 1 = N1 (default), 2 = N2 */
  nivel?: number;
  /** Obligatorio cuando nivel=2 */
  procesoPadreId?: number;
  nombre: string;
  descripcion?: string;
  objetivos?: string;
  estadoDocumentacion?: EstadoDocumentacion;
}

export interface SubprocesoRequest {
  /** Obligatorio para nivel=1; opcional para nivel=2 (se hereda del padre) */
  procesoId?: number;
  /** 1 = SP-N1 (default), 2 = SP-N2 */
  nivel?: number;
  /** Obligatorio cuando nivel=2 */
  subprocesoPadreId?: number;
  nombre: string;
  descripcion?: string;
  estadoDocumentacion?: EstadoDocumentacion;
}

export interface ObjetivoEspecifico {
  id?: number;
  nombre: string;
  descripcion: string;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  creadoPor?: string;
  actualizadoPor?: string;
}

export interface ObjetivoEspecificoRequest {
  nombre: string;
  descripcion: string;
}
