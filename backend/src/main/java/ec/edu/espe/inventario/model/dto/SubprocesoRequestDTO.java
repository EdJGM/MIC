package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear/actualizar Subprocesos (SP-N1 o SP-N2)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubprocesoRequestDTO {

    /**
     * ID del Proceso N2 al que pertenece.
     * - Obligatorio cuando nivel=1.
     * - Para nivel=2 es opcional: si no se envía, se hereda del subprocesoPadre.
     */
    private Long procesoId;

    /**
     * Nivel: 1 = Subproceso N1, 2 = Subproceso N2.
     * Si no se envía, el backend asume nivel 1.
     */
    private Integer nivel;

    /**
     * ID del Subproceso N1 padre. Obligatorio cuando nivel=2.
     */
    private Long subprocesoPadreId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;

    private EstadoDocumentacion estadoDocumentacion;
}
