package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.TipoNotificacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Notificacion
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionResponseDTO {

    private Long id;
    private TipoNotificacion tipo;
    private String tipoNombre;
    private String titulo;
    private String mensaje;
    private String destinatarioId;
    private Boolean leida;
    private LocalDateTime fechaCreacion;
    private Long entidadId;
    private String modulo;
}
