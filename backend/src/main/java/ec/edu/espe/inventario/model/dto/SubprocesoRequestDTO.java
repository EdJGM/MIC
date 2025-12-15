package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear/actualizar Subprocesos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubprocesoRequestDTO {
    
    @NotNull(message = "El proceso es obligatorio")
    private Long procesoId;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    
    private EstadoDocumentacion estadoDocumentacion;
}
