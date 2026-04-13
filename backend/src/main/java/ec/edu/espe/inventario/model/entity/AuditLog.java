package ec.edu.espe.inventario.model.entity;

import ec.edu.espe.inventario.model.enums.AccionAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Log de auditoría — registra todas las operaciones críticas del sistema.
 * Solo lectura después de su creación según ERS (sección 3.3.4 - RF-36).
 */
@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 150, updatable = false)
    @NotBlank(message = "El usuarioId es obligatorio")
    private String usuarioId;

    @Column(nullable = false, length = 200, updatable = false)
    private String usuarioNombre;

    @Column(nullable = false, updatable = false)
    @NotNull(message = "La acción es obligatoria")
    @Enumerated(EnumType.STRING)
    private AccionAudit accion;

    /**
     * Módulo del sistema donde se realizó la operación.
     * Ej: MACROPROCESO, PROCESO, USUARIO_LOCAL, PARAMETRO, NOTIFICACION
     */
    @Column(nullable = false, length = 100, updatable = false)
    @NotBlank(message = "El módulo es obligatorio")
    private String modulo;

    /** ID de la entidad afectada (puede ser null en operaciones de listado) */
    @Column(updatable = false)
    private Long entidadId;

    @Column(columnDefinition = "TEXT", updatable = false)
    private String descripcion;

    /** SUCCESS o ERROR */
    @Column(nullable = false, length = 20, updatable = false)
    private String resultado;

    @Column(length = 50, updatable = false)
    private String ip;

    @PrePersist
    protected void onCreate() {
        fechaHora = LocalDateTime.now();
    }
}
