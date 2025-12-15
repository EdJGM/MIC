package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.model.enums.TipoMacroproceso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear/actualizar Macroprocesos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MacroprocesoRequestDTO {
    
    @NotNull(message = "El tipo es obligatorio")
    private TipoMacroproceso tipo;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    
    @NotBlank(message = "La unidad estratégica es obligatoria")
    private String unidadEstrategica;
    
    @NotBlank(message = "El responsable principal es obligatorio")
    private String responsablePrincipal;
    
    private String objetivosEstrategicos;
    
    private EstadoDocumentacion estadoDocumentacion;
}
