package ec.edu.espe.inventario.model.entity;

import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Proceso - soporta Nivel N1 y N2 dentro de la jerarquía de procesos
 * nivel=1 → Proceso N1 (hijo directo del Macroproceso)
 * nivel=2 → Proceso N2 (hijo de un Proceso N1)
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

    @Column(unique = true, nullable = false, length = 100)
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

    /** 1 = Proceso N1, 2 = Proceso N2 */
    @Column(nullable = false)
    private Integer nivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "macroproceso_id", nullable = false)
    @NotNull(message = "El macroproceso es obligatorio")
    private Macroproceso macroproceso;

    /** Proceso N1 padre (solo para nivel=2; null si nivel=1) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proceso_padre_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Proceso procesoPadre;

    /** Procesos N2 hijos (solo poblado cuando nivel=1) */
    @OneToMany(mappedBy = "procesoPadre", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Proceso> procesosHijos = new ArrayList<>();

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

    /** Subprocesos N1 (hijos directos, solo aplica para Proceso N2) */
    @OneToMany(mappedBy = "proceso", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Subproceso> subprocesos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (nivel == null) {
            nivel = 1;
        }
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
     * Calcula el porcentaje de avance:
     * - N1: promedio de sus Procesos N2 hijos (o su propio estado si no tiene hijos)
     * - N2: promedio de sus Subprocesos N1 hijos (o su propio estado si no tiene hijos)
     */
    public void calcularPorcentajeAvance() {
        if (nivel != null && nivel == 1) {
            if (procesosHijos == null || procesosHijos.isEmpty()) {
                this.porcentajeAvance = estadoDocumentacion.getPorcentajeAsociado();
            } else {
                double promedio = procesosHijos.stream()
                    .mapToInt(Proceso::getPorcentajeAvance)
                    .average()
                    .orElse(0.0);
                this.porcentajeAvance = (int) Math.round(promedio);
            }
        } else {
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
}
