package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.RolLocal;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear/actualizar UsuarioLocal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLocalRequestDTO {

    @NotBlank(message = "El ID externo es obligatorio")
    private String externalId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    @NotNull(message = "El rol es obligatorio")
    private RolLocal rolLocal;

    @NotBlank(message = "La unidad asignada es obligatoria")
    private String unidadAsignada;
}
