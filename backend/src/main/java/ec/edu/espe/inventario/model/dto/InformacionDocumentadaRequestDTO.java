package ec.edu.espe.inventario.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformacionDocumentadaRequestDTO {
    
    private LocalDate fechaSolicitud;
    private String unidad;
    private String solicitadoPor;
    private String sede;
    private Long macroprocesoId;
    private Long procesoN1Id;
    private Long procesoN2Id;
    private Long subprocesoN1Id;
    private Long subprocesoN2Id;
    private String tipoDocumento;
    private String nombreDocumento;
    private LocalDate fechaProtocolo;
    private String lugarEvento;
    private String enlaceArchivo;
    private String motivo;
    private String observaciones;
    private String codigoDocumento;
    private String codigoProceso;
    private String estado;
    private LocalDate fechaEliminacion;
    private String observacionesUpdi;
    private String codificadoPor;
    private Integer mes;
    private Integer anio;
    private String version;
    private Integer secuencial;
    private Long documentoOrigenId;
}
