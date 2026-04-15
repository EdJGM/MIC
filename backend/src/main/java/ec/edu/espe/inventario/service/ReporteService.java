package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.ReporteDTO.*;
import ec.edu.espe.inventario.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteService {

    private final MacroprocesoRepository macroprocesoRepo;
    private final ProcesoRepository procesoRepo;
    private final SubprocesoRepository subprocesoRepo;
    private final InformacionDocumentadaRepository infDocRepo;

    // ── RF-28: Procesos por estado ──────────────────────────────────────────
    public ReporteProcesosEstado reporteProcesosEstado(String unidad, LocalDate fechaDesde, LocalDate fechaHasta) {
        List<ec.edu.espe.inventario.model.entity.Proceso> todos;
        if (fechaDesde != null && fechaHasta != null) {
            LocalDateTime desde = fechaDesde.atStartOfDay();
            LocalDateTime hasta = fechaHasta.atTime(23, 59, 59);
            todos = (unidad != null && !unidad.isBlank())
                    ? procesoRepo.findByUnidadEstrategicaAndFecha(unidad, desde, hasta)
                    : procesoRepo.findByFechaCreacionBetween(desde, hasta);
        } else {
            todos = (unidad != null && !unidad.isBlank())
                    ? procesoRepo.findByUnidadEstrategica(unidad)
                    : procesoRepo.findAll();
        }

        Map<String, Long> conteo = todos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getEstadoDocumentacion().getDescripcion(),
                        Collectors.counting()));

        long total = todos.size();
        double promedio = todos.stream().mapToInt(p -> p.getPorcentajeAvance()).average().orElse(0);

        List<GrupoConteo> grupos = conteo.entrySet().stream()
                .map(e -> new GrupoConteo(e.getKey(), e.getValue(),
                        total > 0 ? (e.getValue() * 100.0 / total) : 0))
                .sorted(Comparator.comparing(GrupoConteo::getGrupo))
                .collect(Collectors.toList());

        return new ReporteProcesosEstado(grupos, (long) total, Math.round(promedio * 10.0) / 10.0);
    }

    // ── RF-29: Procesos por nivel jerárquico ────────────────────────────────
    public ReporteProcesosNivel reporteProcesosNivel(LocalDate fechaDesde, LocalDate fechaHasta) {
        if (fechaDesde != null && fechaHasta != null) {
            LocalDateTime desde = fechaDesde.atStartOfDay();
            LocalDateTime hasta = fechaHasta.atTime(23, 59, 59);
            var procesos = procesoRepo.findByFechaCreacionBetween(desde, hasta);
            long n1 = procesos.stream().filter(p -> p.getNivel() == 1).count();
            long n2 = procesos.stream().filter(p -> p.getNivel() == 2).count();
            long macros = procesos.stream().map(p -> p.getMacroproceso().getId()).distinct().count();
            List<GrupoConteo> macroporc = procesos.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getMacroproceso().getCodigo() + " — " + p.getMacroproceso().getNombre(),
                            Collectors.counting()))
                    .entrySet().stream()
                    .map(e -> new GrupoConteo(e.getKey(), e.getValue(), 0.0))
                    .sorted(Comparator.comparing(GrupoConteo::getGrupo))
                    .collect(Collectors.toList());
            return new ReporteProcesosNivel(macros, n1, n2, 0L, 0L, macroporc);
        }

        long macros = macroprocesoRepo.count();
        long n1 = procesoRepo.countByNivel(1);
        long n2 = procesoRepo.countByNivel(2);
        long spN1 = subprocesoRepo.countByNivel(1);
        long spN2 = subprocesoRepo.countByNivel(2);

        List<GrupoConteo> macroporc = macroprocesoRepo.findAll().stream()
                .map(m -> new GrupoConteo(
                        m.getCodigo() + " — " + m.getNombre(),
                        (long) m.getProcesos().size(),
                        (double) m.getPorcentajeAvance()))
                .sorted(Comparator.comparing(GrupoConteo::getGrupo))
                .collect(Collectors.toList());

        return new ReporteProcesosNivel(macros, n1, n2, spN1, spN2, macroporc);
    }

    // ── RF-30: Procesos por unidad ──────────────────────────────────────────
    public List<GrupoConteo> reporteProcesosUnidad(LocalDate fechaDesde, LocalDate fechaHasta) {
        Map<String, Long> conteo;
        if (fechaDesde != null && fechaHasta != null) {
            LocalDateTime desde = fechaDesde.atStartOfDay();
            LocalDateTime hasta = fechaHasta.atTime(23, 59, 59);
            var procesos = procesoRepo.findByFechaCreacionBetween(desde, hasta);
            conteo = procesos.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getMacroproceso().getUnidadEstrategica(),
                            Collectors.counting()));
        } else {
            conteo = macroprocesoRepo.findAll().stream()
                    .collect(Collectors.groupingBy(
                            m -> m.getUnidadEstrategica(),
                            Collectors.summingLong(m -> m.getProcesos().size())));
        }
        long total = conteo.values().stream().mapToLong(Long::longValue).sum();
        return conteo.entrySet().stream()
                .map(e -> new GrupoConteo(e.getKey(), e.getValue(),
                        total > 0 ? (e.getValue() * 100.0 / total) : 0))
                .sorted(Comparator.comparing(GrupoConteo::getGrupo))
                .collect(Collectors.toList());
    }

    // ── RF-24: Documentos por estado ────────────────────────────────────────
    public ReporteDocumentosEstado reporteDocumentosEstado() {
        long activos     = infDocRepo.countByEstado("ACTIVO");
        long suspendidos = infDocRepo.countByEstado("SUSPENDIDO");
        long obsoletos   = infDocRepo.countByEstado("OBSOLETO");
        return new ReporteDocumentosEstado(activos, suspendidos, obsoletos,
                activos + suspendidos + obsoletos);
    }

    // ── RF-25: Documentos por tipo ──────────────────────────────────────────
    public ReporteDocumentosTipo reporteDocumentosTipo() {
        var grupos = infDocRepo.countGroupByTipoDocumento();
        long total = grupos.stream().mapToLong(g -> (Long) g[1]).sum();
        List<GrupoConteo> lista = grupos.stream()
                .map(g -> new GrupoConteo((String) g[0], (Long) g[1],
                        total > 0 ? ((Long) g[1] * 100.0 / total) : 0))
                .collect(Collectors.toList());
        return new ReporteDocumentosTipo(lista, total);
    }

    // ── RF-26: Documentos por unidad ────────────────────────────────────────
    public ReporteDocumentosUnidad reporteDocumentosUnidad() {
        var grupos = infDocRepo.countGroupByUnidad();
        long total = grupos.stream().mapToLong(g -> (Long) g[1]).sum();
        List<GrupoConteo> lista = grupos.stream()
                .map(g -> new GrupoConteo((String) g[0], (Long) g[1],
                        total > 0 ? ((Long) g[1] * 100.0 / total) : 0))
                .collect(Collectors.toList());
        return new ReporteDocumentosUnidad(lista, total);
    }

    // ── RF-27: Documentos por fecha ─────────────────────────────────────────
    public ReporteDocumentosFecha reporteDocumentosFecha(Integer anioDesde, Integer anioHasta) {
        var grupos = infDocRepo.countGroupByAnioMes(
                anioDesde != null ? anioDesde : 2020,
                anioHasta != null ? anioHasta : 2030);
        long total = grupos.stream().mapToLong(g -> ((Number) g[2]).longValue()).sum();
        List<GrupoConteo> lista = grupos.stream()
                .map(g -> new GrupoConteo(
                        g[0] + "/" + String.format("%02d", ((Number) g[1]).intValue()),
                        ((Number) g[2]).longValue(),
                        total > 0 ? (((Number) g[2]).longValue() * 100.0 / total) : 0))
                .collect(Collectors.toList());
        return new ReporteDocumentosFecha(lista, total,
                anioDesde + "-01", anioHasta + "-12");
    }
}
