package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.AuditLogResponseDTO;
import ec.edu.espe.inventario.model.entity.AuditLog;
import ec.edu.espe.inventario.model.enums.AccionAudit;
import ec.edu.espe.inventario.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para el log de auditoría del sistema según ERS (RF-36)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Registrar una operación en el log de auditoría.
     * Se llama desde los demás servicios al ejecutar operaciones críticas.
     */
    @Transactional
    public void registrar(String usuarioId, String usuarioNombre, AccionAudit accion,
                          String modulo, Long entidadId, String descripcion) {
        registrar(usuarioId, usuarioNombre, accion, modulo, entidadId, descripcion, "SUCCESS", null);
    }

    @Transactional
    public void registrar(String usuarioId, String usuarioNombre, AccionAudit accion,
                          String modulo, Long entidadId, String descripcion,
                          String resultado, String ip) {
        AuditLog log = new AuditLog();
        log.setUsuarioId(usuarioId);
        log.setUsuarioNombre(usuarioNombre);
        log.setAccion(accion);
        log.setModulo(modulo);
        log.setEntidadId(entidadId);
        log.setDescripcion(descripcion);
        log.setResultado(resultado);
        log.setIp(ip);
        auditLogRepository.save(log);
    }

    /**
     * Obtener todos los logs ordenados por fecha descendente
     */
    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los audit logs");
        return auditLogRepository.findAll().stream()
                .sorted((a, b) -> b.getFechaHora().compareTo(a.getFechaHora()))
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Filtrar logs por módulo y rango de fechas
     */
    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO> filtrar(String modulo, LocalDateTime desde, LocalDateTime hasta) {
        log.info("Filtrando audit logs - modulo: {}, desde: {}, hasta: {}", modulo, desde, hasta);

        List<AuditLog> logs;

        if (modulo != null && !modulo.isBlank() && desde != null && hasta != null) {
            logs = auditLogRepository.findByModuloAndFechaHoraBetweenOrderByFechaHoraDesc(modulo, desde, hasta);
        } else if (desde != null && hasta != null) {
            logs = auditLogRepository.findByFechaHoraBetweenOrderByFechaHoraDesc(desde, hasta);
        } else if (modulo != null && !modulo.isBlank()) {
            logs = auditLogRepository.findByModuloOrderByFechaHoraDesc(modulo);
        } else {
            logs = auditLogRepository.findAll().stream()
                    .sorted((a, b) -> b.getFechaHora().compareTo(a.getFechaHora()))
                    .collect(Collectors.toList());
        }

        return logs.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    /**
     * Convertir entidad a DTO
     */
    private AuditLogResponseDTO convertirADTO(AuditLog auditLog) {
        AuditLogResponseDTO dto = new AuditLogResponseDTO();
        dto.setId(auditLog.getId());
        dto.setFechaHora(auditLog.getFechaHora());
        dto.setUsuarioId(auditLog.getUsuarioId());
        dto.setUsuarioNombre(auditLog.getUsuarioNombre());
        dto.setAccion(auditLog.getAccion());
        dto.setAccionDescripcion(auditLog.getAccion().getDescripcion());
        dto.setModulo(auditLog.getModulo());
        dto.setEntidadId(auditLog.getEntidadId());
        dto.setDescripcion(auditLog.getDescripcion());
        dto.setResultado(auditLog.getResultado());
        dto.setIp(auditLog.getIp());
        return dto;
    }
}
