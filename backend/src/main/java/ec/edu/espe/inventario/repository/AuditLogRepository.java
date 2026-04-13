package ec.edu.espe.inventario.repository;

import ec.edu.espe.inventario.model.entity.AuditLog;
import ec.edu.espe.inventario.model.enums.AccionAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para AuditLog
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUsuarioIdOrderByFechaHoraDesc(String usuarioId);

    List<AuditLog> findByModuloOrderByFechaHoraDesc(String modulo);

    List<AuditLog> findByAccionOrderByFechaHoraDesc(AccionAudit accion);

    List<AuditLog> findByFechaHoraBetweenOrderByFechaHoraDesc(LocalDateTime desde, LocalDateTime hasta);

    List<AuditLog> findByModuloAndFechaHoraBetweenOrderByFechaHoraDesc(
            String modulo, LocalDateTime desde, LocalDateTime hasta);
}
