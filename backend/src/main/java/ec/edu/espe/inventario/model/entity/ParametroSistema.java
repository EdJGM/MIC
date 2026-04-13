package ec.edu.espe.inventario.model.entity;

import ec.edu.espe.inventario.model.enums.TipoParametro;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Parámetros de configuración global del sistema según ERS (sección 3.3.4 - RF-35)
 * Ejemplos: DIAS_ALERTA_VENCIMIENTO, PLAZO_MAX_REVISION, etc.
 */
@Entity
@Table(name = "parametros_sistema")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametroSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    @NotBlank(message = "La clave es obligatoria")
    @Size(max = 100, message = "La clave no puede exceder 100 caracteres")
    private String clave;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "El valor es obligatorio")
    private String valor;

    @Column(nullable = false, length = 300)
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 300, message = "La descripción no puede exceder 300 caracteres")
    private String descripcion;

    @Column(nullable = false)
    @NotNull(message = "El tipo es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoParametro tipo;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(nullable = false, length = 100)
    private String modificadoPor;

    @PrePersist
    protected void onCreate() {
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
