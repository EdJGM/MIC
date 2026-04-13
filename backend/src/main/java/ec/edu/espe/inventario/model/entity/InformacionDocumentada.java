package ec.edu.espe.inventario.model.entity;

import ec.edu.espe.inventario.model.enums.EstadoDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "informacion_documentada")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformacionDocumentada {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Column(name = "unidad", nullable = false, length = 200)
    private String unidad;

    @Column(name = "solicitado_por", nullable = false, length = 200)
    private String solicitadoPor;

    @Column(name = "sede", nullable = false, length = 50)
    private String sede; // MATRIZ, LATACUNGA, SANTO_DOMINGO

    @ManyToOne
    @JoinColumn(name = "macroproceso_id")
    private Macroproceso macroproceso;

    @ManyToOne
    @JoinColumn(name = "proceso_n1_id")
    private Proceso procesoN1;

    @ManyToOne
    @JoinColumn(name = "proceso_n2_id")
    private Proceso procesoN2;

    @ManyToOne
    @JoinColumn(name = "subproceso_n1_id")
    private Subproceso subprocesoN1;

    @ManyToOne
    @JoinColumn(name = "subproceso_n2_id")
    private Subproceso subprocesoN2;

    @Column(name = "tipo_documento", nullable = false, length = 100)
    private String tipoDocumento; // Actas, Acuerdos, Protocolos, Bitácora, Formatos, Informes, Lineamientos, etc.

    @Column(name = "nombre_documento", nullable = false, length = 300)
    private String nombreDocumento;

    @Column(name = "fecha_protocolo")
    private LocalDate fechaProtocolo;

    @Column(name = "lugar_evento", length = 300)
    private String lugarEvento;

    @Column(name = "enlace_archivo", length = 500)
    private String enlaceArchivo;

    @Column(name = "motivo", nullable = false, length = 50)
    private String motivo; // CREACION, ACTUALIZACION, ELIMINACION

    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    @Column(name = "codigo_documento", unique = true, length = 100)
    private String codigoDocumento; // UNIDAD-TIPODOC-ANIO-VERSION-SECUENCIA

    @Column(name = "codigo_proceso", length = 50)
    private String codigoProceso;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoDocumento estado; // ACTIVO, INACTIVO, ELIMINADO

    @Column(name = "fecha_eliminacion")
    private LocalDate fechaEliminacion;

    @Column(name = "observaciones_updi", length = 1000)
    private String observacionesUpdi;

    @Column(name = "codificado_por", length = 100)
    private String codificadoPor;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "version", length = 10)
    private String version;

    @Column(name = "secuencial")
    private Integer secuencial;

    @Column(name = "documento_origen_id")
    private Long documentoOrigenId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (fechaSolicitud == null) {
            fechaSolicitud = LocalDate.now();
        }
        if (estado == null) {
            estado = EstadoDocumento.ACTIVO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
