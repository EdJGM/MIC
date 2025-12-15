package ec.edu.espe.inventario.model.entity;

import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Proceso - Nivel 2 de la jerarquía de procesos
 */
@Entity
@Table(name = "procesos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proceso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 30)
    @NotBlank(message = "El código es obligatorio")
    private String codigo;
    
    @Column(nullable = false, length = 200)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    
    @Column(columnDefinition = "TEXT")
    private String objetivos;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "macroproceso_id", nullable = false)
    @NotNull(message = "El macroproceso es obligatorio")
    private Macroproceso macroproceso;
    
    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoDocumentacion estadoDocumentacion;
    
    @Column(nullable = false)
    private Integer porcentajeAvance;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;
    
    @Column(nullable = false, length = 100)
    private String creadoPor;
    
    @Column(nullable = false, length = 100)
    private String actualizadoPor;
    
    @OneToMany(mappedBy = "proceso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subproceso> subprocesos = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (estadoDocumentacion == null) {
            estadoDocumentacion = EstadoDocumentacion.NO_DOCUMENTADO;
        }
        if (porcentajeAvance == null) {
            porcentajeAvance = 0;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    /**
     * Calcula el porcentaje de avance promedio basado en los subprocesos
     */
    public void calcularPorcentajeAvance() {
        if (subprocesos == null || subprocesos.isEmpty()) {
            this.porcentajeAvance = estadoDocumentacion.getPorcentajeAsociado();
        } else {
            double promedio = subprocesos.stream()
                .mapToInt(Subproceso::getPorcentajeAvance)
                .average()
                .orElse(0.0);
            this.porcentajeAvance = (int) Math.round(promedio);
        }
    }
}
