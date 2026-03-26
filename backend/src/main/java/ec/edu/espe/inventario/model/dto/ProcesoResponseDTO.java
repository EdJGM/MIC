package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuestas de Procesos (N1 o N2)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcesoResponseDTO {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String objetivos;

    /** 1 = Proceso N1, 2 = Proceso N2 */
    private Integer nivel;

    private Long macroprocesoId;
    private String macroprocesoNombre;

    /** Datos del Proceso N1 padre (solo cuando nivel=2) */
    private Long procesoPadreId;
    private String procesoPadreNombre;

    private EstadoDocumentacion estadoDocumentacion;
    private Integer porcentajeAvance;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String creadoPor;
    private String actualizadoPor;

    /** Para Proceso N1: cantidad de Procesos N2 hijos */
    private Integer cantidadProcesosHijos;

    /** Para Proceso N2: cantidad de Subprocesos N1 hijos */
    private Integer cantidadSubprocesos;

    private List<SubprocesoResponseDTO> subprocesos;
}
