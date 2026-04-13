package ec.edu.espe.inventario.controller;

import ec.edu.espe.inventario.model.dto.AuditLogResponseDTO;
import ec.edu.espe.inventario.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para consulta del log de auditoría del sistema
 */
@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class AuditLogController {

    private final AuditLogService auditLogService;

    /**
     * Obtener todos los logs de auditoría con filtros opcionales
     * GET /api/audit
     * Parámetros opcionales: modulo, desde, hasta
     */
    @GetMapping
    public ResponseEntity<List<AuditLogResponseDTO>> obtener(
            @RequestParam(required = false) String modulo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        log.info("GET /api/audit - modulo={}, desde={}, hasta={}", modulo, desde, hasta);

        if (modulo != null || desde != null || hasta != null) {
            return ResponseEntity.ok(auditLogService.filtrar(modulo, desde, hasta));
        }
        return ResponseEntity.ok(auditLogService.obtenerTodos());
    }
}
