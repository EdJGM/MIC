package ec.edu.espe.inventario.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar el valor de un ParametroSistema.
 * La clave y el tipo no se modifican — solo el valor.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametroSistemaRequestDTO {

    @NotBlank(message = "El valor es obligatorio")
    @Size(max = 500, message = "El valor no puede exceder 500 caracteres")
    private String valor;
}
