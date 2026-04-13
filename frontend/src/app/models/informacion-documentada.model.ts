export interface InformacionDocumentada {
  id?: number;
  fechaSolicitud: Date | string;
  unidad: string;
  solicitadoPor: string;
  sede: string;
  macroprocesoId?: number;
  macroprocesoNombre?: string;
  procesoN1Id?: number;
  procesoN1Nombre?: string;
  procesoN2Id?: number;
  procesoN2Nombre?: string;
  subprocesoN1Id?: number;
  subprocesoN1Nombre?: string;
  subprocesoN2Id?: number;
  subprocesoN2Nombre?: string;
  tipoDocumento: string;
  nombreDocumento: string;
  fechaProtocolo?: Date | string;
  lugarEvento?: string;
  enlaceArchivo?: string;
  motivo: string;
  observaciones?: string;
  codigoDocumento?: string;
  codigoProceso?: string;
  estado?: string;
  fechaEliminacion?: Date | string;
  observacionesUpdi?: string;
  codificadoPor?: string;
  mes?: number;
  anio?: number;
  version?: string;
  secuencial?: number;
  createdAt?: Date | string;
  updatedAt?: Date | string;
  documentoOrigenId?: number;
  cantidadVersiones?: number;
}

export interface InformacionDocumentadaRequest {
  fechaSolicitud: string;
  unidad: string;
  solicitadoPor: string;
  sede: string;
  macroprocesoId?: number;
  procesoN1Id?: number;
  procesoN2Id?: number;
  subprocesoN1Id?: number;
  subprocesoN2Id?: number;
  tipoDocumento: string;
  nombreDocumento: string;
  fechaProtocolo?: string;
  lugarEvento?: string;
  enlaceArchivo?: string;
  motivo: string;
  observaciones?: string;
  codigoDocumento?: string;
  codigoProceso?: string;
  estado?: string;
  observacionesUpdi?: string;
  codificadoPor?: string;
  mes?: number;
  anio?: number;
  version?: string;
  secuencial?: number;
  documentoOrigenId?: number;
}

export const SEDES = [
  { label: 'Matriz', value: 'MATRIZ' },
  { label: 'Latacunga', value: 'LATACUNGA' },
  { label: 'Santo Domingo', value: 'SANTO_DOMINGO' }
];

export const TIPOS_DOCUMENTO = [
  { label: 'Actas', value: 'ACTAS' },
  { label: 'Acuerdos', value: 'ACUERDOS' },
  { label: 'Protocolos', value: 'PROTOCOLOS' },
  { label: 'Bitácora', value: 'BITACORA' },
  { label: 'Formatos', value: 'FORMATOS' },
  { label: 'Informes', value: 'INFORMES' },
  { label: 'Lineamientos', value: 'LINEAMIENTOS' },
  { label: 'Instructivos', value: 'INSTRUCTIVOS' },
  { label: 'Procedimientos', value: 'PROCEDIMIENTOS' },
  { label: 'Guías', value: 'GUIAS' }
];

export const MOTIVOS = [
  { label: 'Creación', value: 'CREACION' },
  { label: 'Actualización', value: 'ACTUALIZACION' },
  { label: 'Eliminación', value: 'ELIMINACION' }
];

export const ESTADOS_DOCUMENTO = [
  { label: 'Activo', value: 'ACTIVO' },
  { label: 'Inactivo', value: 'INACTIVO' },
  { label: 'Eliminado', value: 'ELIMINADO' }
];
