package ec.edu.espe.inventario.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ObjetivoEspecificoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    @Size(max = 1000, message = "La descripcion no puede exceder 1000 caracteres")
    private String descripcion;
}
