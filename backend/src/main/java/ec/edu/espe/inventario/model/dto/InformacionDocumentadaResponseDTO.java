package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.EstadoDocumento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformacionDocumentadaResponseDTO {
    
    private Long id;
    private LocalDate fechaSolicitud;
    private String unidad;
    private String solicitadoPor;
    private String sede;
    private Long macroprocesoId;
    private String macroprocesoNombre;
    private Long procesoN1Id;
    private String procesoN1Nombre;
    private Long procesoN2Id;
    private String procesoN2Nombre;
    private Long subprocesoN1Id;
    private String subprocesoN1Nombre;
    private Long subprocesoN2Id;
    private String subprocesoN2Nombre;
    private String tipoDocumento;
    private String nombreDocumento;
    private LocalDate fechaProtocolo;
    private String lugarEvento;
    private String enlaceArchivo;
    private String motivo;
    private String observaciones;
    private String codigoDocumento;
    private String codigoProceso;
    private EstadoDocumento estado;
    private LocalDate fechaEliminacion;
    private String observacionesUpdi;
    private String codificadoPor;
    private Integer mes;
    private Integer anio;
    private String version;
    private Integer secuencial;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
