package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuestas de Subprocesos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubprocesoResponseDTO {
    
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Long procesoId;
    private String procesoNombre;
    private EstadoDocumentacion estadoDocumentacion;
    private Integer porcentajeAvance;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String creadoPor;
    private String actualizadoPor;
}
