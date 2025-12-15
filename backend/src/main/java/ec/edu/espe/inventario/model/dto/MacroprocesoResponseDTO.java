package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.model.enums.TipoMacroproceso;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuestas de Macroprocesos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MacroprocesoResponseDTO {
    
    private Long id;
    private String codigo;
    private TipoMacroproceso tipo;
    private String nombre;
    private String descripcion;
    private String unidadEstrategica;
    private String responsablePrincipal;
    private String objetivosEstrategicos;
    private EstadoDocumentacion estadoDocumentacion;
    private Integer porcentajeAvance;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String creadoPor;
    private String actualizadoPor;
    private Integer cantidadProcesos;
    private List<ProcesoResponseDTO> procesos;
}
