package ec.edu.espe.inventario.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

public class ReporteDTO {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class GrupoConteo {
        private String grupo;
        private Long cantidad;
        private Double porcentaje;
    }

    /** RF-28 */
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ReporteProcesosEstado {
        private List<GrupoConteo> porEstado;
        private Long totalProcesos;
        private Double promedioAvance;
    }

    /** RF-29 */
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ReporteProcesosNivel {
        private Long totalMacroprocesos;
        private Long totalProcesosN1;
        private Long totalProcesosN2;
        private Long totalSubprocesosN1;
        private Long totalSubprocesosN2;
        private List<GrupoConteo> macroporcentajes;
    }

    /** RF-24 */
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ReporteDocumentosEstado {
        private Long activos;
        private Long suspendidos;
        private Long obsoletos;
        private Long total;
    }

    /** RF-25 */
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ReporteDocumentosTipo {
        private List<GrupoConteo> porTipo;
        private Long total;
    }

    /** RF-26 */
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ReporteDocumentosUnidad {
        private List<GrupoConteo> porUnidad;
        private Long total;
    }

    /** RF-27 */
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ReporteDocumentosFecha {
        private List<GrupoConteo> porMes;
        private Long total;
        private String fechaDesde;
        private String fechaHasta;
    }
}
