package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.RolLocal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para UsuarioLocal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLocalResponseDTO {

    private Long id;
    private String externalId;
    private String nombre;
    private String email;
    private String telefono;
    private RolLocal rolLocal;
    private String rolNombre;
    private String unidadAsignada;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String creadoPor;
    private String actualizadoPor;
}
