package ec.edu.espe.inventario.controller;

import ec.edu.espe.inventario.model.dto.ReporteDTO.*;
import ec.edu.espe.inventario.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ReporteController {

    private final ReporteService reporteService;

    // RF-28
    @GetMapping("/procesos/estado")
    public ResponseEntity<ReporteProcesosEstado> procesosPorEstado(
            @RequestParam(required = false) String unidad,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {
        return ResponseEntity.ok(reporteService.reporteProcesosEstado(unidad, fechaDesde, fechaHasta));
    }

    // RF-29
    @GetMapping("/procesos/nivel")
    public ResponseEntity<ReporteProcesosNivel> procesosPorNivel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {
        return ResponseEntity.ok(reporteService.reporteProcesosNivel(fechaDesde, fechaHasta));
    }

    // RF-30
    @GetMapping("/procesos/unidad")
    public ResponseEntity<List<GrupoConteo>> procesosPorUnidad(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {
        return ResponseEntity.ok(reporteService.reporteProcesosUnidad(fechaDesde, fechaHasta));
    }

    // RF-24
    @GetMapping("/documentos/estado")
    public ResponseEntity<ReporteDocumentosEstado> documentosPorEstado() {
        return ResponseEntity.ok(reporteService.reporteDocumentosEstado());
    }

    // RF-25
    @GetMapping("/documentos/tipo")
    public ResponseEntity<ReporteDocumentosTipo> documentosPorTipo() {
        return ResponseEntity.ok(reporteService.reporteDocumentosTipo());
    }

    // RF-26
    @GetMapping("/documentos/unidad")
    public ResponseEntity<ReporteDocumentosUnidad> documentosPorUnidad() {
        return ResponseEntity.ok(reporteService.reporteDocumentosUnidad());
    }

    // RF-27
    @GetMapping("/documentos/fecha")
    public ResponseEntity<ReporteDocumentosFecha> documentosPorFecha(
            @RequestParam(required = false) Integer anioDesde,
            @RequestParam(required = false) Integer anioHasta) {
        return ResponseEntity.ok(reporteService.reporteDocumentosFecha(anioDesde, anioHasta));
    }
}
