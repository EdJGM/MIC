package ec.edu.espe.inventario.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ObjetivoEspecificoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String creadoPor;
    private String actualizadoPor;
}
