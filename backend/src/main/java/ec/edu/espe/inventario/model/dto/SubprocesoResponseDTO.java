package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de Subprocesos (SP-N1 o SP-N2)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubprocesoResponseDTO {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;

    /** 1 = Subproceso N1, 2 = Subproceso N2 */
    private Integer nivel;

    private Long procesoId;
    private String procesoNombre;

    /** Datos del Subproceso N1 padre (solo cuando nivel=2) */
    private Long subprocesoPadreId;
    private String subprocesoPadreNombre;

    private EstadoDocumentacion estadoDocumentacion;
    private Integer porcentajeAvance;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String creadoPor;
    private String actualizadoPor;

    /** Para SP-N1: cantidad de SP-N2 hijos */
    private Integer cantidadSubprocesosHijos;
}
