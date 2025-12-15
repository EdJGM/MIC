package ec.edu.espe.inventario.model.entity;

import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.model.enums.TipoMacroproceso;
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
 * Entidad Macroproceso - Nivel 1 de la jerarquía de procesos
 */
@Entity
@Table(name = "macroprocesos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Macroproceso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 30)
    @NotBlank(message = "El código es obligatorio")
    private String codigo;
    
    @Column(nullable = false)
    @NotNull(message = "El tipo es obligatorio")
    @Enumerated(EnumType.STRING)
    private TipoMacroproceso tipo;
    
    @Column(nullable = false, length = 200)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;
    
    @Column(nullable = false, length = 100)
    @NotBlank(message = "La unidad estratégica es obligatoria")
    private String unidadEstrategica;
    
    @Column(nullable = false, length = 150)
    @NotBlank(message = "El responsable principal es obligatorio")
    private String responsablePrincipal;
    
    @Column(columnDefinition = "TEXT")
    private String objetivosEstrategicos;
    
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
    
    @OneToMany(mappedBy = "macroproceso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Proceso> procesos = new ArrayList<>();
    
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
     * Calcula el porcentaje de avance promedio basado en los procesos
     */
    public void calcularPorcentajeAvance() {
        if (procesos == null || procesos.isEmpty()) {
            this.porcentajeAvance = estadoDocumentacion.getPorcentajeAsociado();
        } else {
            double promedio = procesos.stream()
                .mapToInt(Proceso::getPorcentajeAvance)
                .average()
                .orElse(0.0);
            this.porcentajeAvance = (int) Math.round(promedio);
        }
    }
}
