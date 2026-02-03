package ec.edu.espe.inventario.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad ObjetivoEspecifico - Objetivos que pueden ser asignados a macroprocesos
 */
@Entity
@Table(name = "objetivos_especificos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjetivoEspecifico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    @Column(columnDefinition = "TEXT")
    @Size(max = 1000, message = "La descripcion no puede exceder 1000 caracteres")
    private String descripcion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(nullable = false, length = 100)
    private String creadoPor;

    @Column(nullable = false, length = 100)
    private String actualizadoPor;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
