package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.AccionAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para AuditLog — solo lectura
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponseDTO {

    private Long id;
    private LocalDateTime fechaHora;
    private String usuarioId;
    private String usuarioNombre;
    private AccionAudit accion;
    private String accionDescripcion;
    private String modulo;
    private Long entidadId;
    private String descripcion;
    private String resultado;
    private String ip;
}
