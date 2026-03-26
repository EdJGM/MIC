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
 * Entidad Subproceso - soporta Nivel SP-N1 y SP-N2
 * nivel=1 → Subproceso N1 (hijo de Proceso N2)
 * nivel=2 → Subproceso N2 (hijo de Subproceso N1)
 */
@Entity
@Table(name = "subprocesos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subproceso {

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

    /** 1 = Subproceso N1, 2 = Subproceso N2 */
    @Column(nullable = false)
    private Integer nivel;

    /** Proceso N2 al que pertenece (siempre apunta a un Proceso N2) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proceso_id", nullable = false)
    @NotNull(message = "El proceso es obligatorio")
    private Proceso proceso;

    /** Subproceso N1 padre (solo para nivel=2; null si nivel=1) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subproceso_padre_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Subproceso subprocesoPadre;

    /** Subprocesos N2 hijos (solo poblado cuando nivel=1) */
    @OneToMany(mappedBy = "subprocesoPadre", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Subproceso> subprocesosHijos = new ArrayList<>();

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
            porcentajeAvance = estadoDocumentacion.getPorcentajeAsociado();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Calcula el porcentaje de avance:
     * - SP-N1: promedio de sus SP-N2 hijos (o su propio estado si no tiene hijos)
     * - SP-N2: valor directo del estado de documentación
     */
    public void calcularPorcentajeAvance() {
        if (nivel != null && nivel == 1) {
            if (subprocesosHijos == null || subprocesosHijos.isEmpty()) {
                this.porcentajeAvance = estadoDocumentacion.getPorcentajeAsociado();
            } else {
                double promedio = subprocesosHijos.stream()
                    .mapToInt(Subproceso::getPorcentajeAvance)
                    .average()
                    .orElse(0.0);
                this.porcentajeAvance = (int) Math.round(promedio);
            }
        } else {
            this.porcentajeAvance = estadoDocumentacion.getPorcentajeAsociado();
        }
    }
}
