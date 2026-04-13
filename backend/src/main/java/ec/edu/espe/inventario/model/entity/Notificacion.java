package ec.edu.espe.inventario.model.entity;

import ec.edu.espe.inventario.model.enums.TipoNotificacion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Notificaciones del sistema para los usuarios según ERS (sección 3.3.5 - RF-38)
 */
@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "El tipo es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoNotificacion tipo;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    /** externalId del usuario destinatario */
    @Column(nullable = false, length = 150)
    @NotBlank(message = "El destinatario es obligatorio")
    private String destinatarioId;

    @Column(nullable = false)
    private Boolean leida = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /** ID de la entidad relacionada (documento, proceso, etc.) */
    @Column
    private Long entidadId;

    /** Módulo al que pertenece la entidad relacionada */
    @Column(length = 100)
    private String modulo;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (leida == null) {
            leida = false;
        }
    }
}
