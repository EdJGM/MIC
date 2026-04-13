package ec.edu.espe.inventario.repository;

import ec.edu.espe.inventario.model.entity.Notificacion;
import ec.edu.espe.inventario.model.enums.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para Notificacion
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByDestinatarioIdOrderByFechaCreacionDesc(String destinatarioId);

    List<Notificacion> findByDestinatarioIdAndLeidaOrderByFechaCreacionDesc(
            String destinatarioId, Boolean leida);

    List<Notificacion> findByDestinatarioIdAndTipoOrderByFechaCreacionDesc(
            String destinatarioId, TipoNotificacion tipo);

    long countByDestinatarioIdAndLeida(String destinatarioId, Boolean leida);

    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true WHERE n.destinatarioId = :destinatarioId AND n.leida = false")
    void marcarTodasComoLeidas(String destinatarioId);
}
