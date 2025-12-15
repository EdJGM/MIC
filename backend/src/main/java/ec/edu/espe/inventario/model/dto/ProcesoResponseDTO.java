package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuestas de Procesos
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
    private Long macroprocesoId;
    private String macroprocesoNombre;
    private EstadoDocumentacion estadoDocumentacion;
    private Integer porcentajeAvance;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String creadoPor;
    private String actualizadoPor;
    private Integer cantidadSubprocesos;
    private List<SubprocesoResponseDTO> subprocesos;
}
