package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.NotificacionResponseDTO;
import ec.edu.espe.inventario.model.entity.Notificacion;
import ec.edu.espe.inventario.model.enums.TipoNotificacion;
import ec.edu.espe.inventario.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de notificaciones del sistema según ERS (RF-38)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    /**
     * Crear una notificación (llamado internamente por otros servicios)
     */
    @Transactional
    public void crear(TipoNotificacion tipo, String titulo, String mensaje,
                      String destinatarioId, Long entidadId, String modulo) {
        Notificacion notificacion = new Notificacion();
        notificacion.setTipo(tipo);
        notificacion.setTitulo(titulo);
        notificacion.setMensaje(mensaje);
        notificacion.setDestinatarioId(destinatarioId);
        notificacion.setEntidadId(entidadId);
        notificacion.setModulo(modulo);
        notificacion.setLeida(false);
        notificacionRepository.save(notificacion);
        log.info("Notificación creada para {}: {}", destinatarioId, titulo);
    }

    /**
     * Obtener todas las notificaciones de un usuario
     */
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> obtenerPorUsuario(String destinatarioId) {
        log.info("Obteniendo notificaciones del usuario: {}", destinatarioId);
        return notificacionRepository
                .findByDestinatarioIdOrderByFechaCreacionDesc(destinatarioId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener solo notificaciones no leídas de un usuario
     */
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> obtenerNoLeidas(String destinatarioId) {
        log.info("Obteniendo notificaciones no leídas del usuario: {}", destinatarioId);
        return notificacionRepository
                .findByDestinatarioIdAndLeidaOrderByFechaCreacionDesc(destinatarioId, false)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Contar notificaciones no leídas de un usuario
     */
    @Transactional(readOnly = true)
    public long contarNoLeidas(String destinatarioId) {
        return notificacionRepository.countByDestinatarioIdAndLeida(destinatarioId, false);
    }

    /**
     * Marcar una notificación como leída
     */
    @Transactional
    public NotificacionResponseDTO marcarComoLeida(Long id) {
        log.info("Marcando notificación {} como leída", id);
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));

        notificacion.setLeida(true);
        Notificacion updated = notificacionRepository.save(notificacion);
        return convertirADTO(updated);
    }

    /**
     * Marcar todas las notificaciones de un usuario como leídas
     */
    @Transactional
    public void marcarTodasComoLeidas(String destinatarioId) {
        log.info("Marcando todas las notificaciones de {} como leídas", destinatarioId);
        notificacionRepository.marcarTodasComoLeidas(destinatarioId);
    }

    /**
     * Convertir entidad a DTO
     */
    private NotificacionResponseDTO convertirADTO(Notificacion notificacion) {
        NotificacionResponseDTO dto = new NotificacionResponseDTO();
        dto.setId(notificacion.getId());
        dto.setTipo(notificacion.getTipo());
        dto.setTipoNombre(notificacion.getTipo().getNombre());
        dto.setTitulo(notificacion.getTitulo());
        dto.setMensaje(notificacion.getMensaje());
        dto.setDestinatarioId(notificacion.getDestinatarioId());
        dto.setLeida(notificacion.getLeida());
        dto.setFechaCreacion(notificacion.getFechaCreacion());
        dto.setEntidadId(notificacion.getEntidadId());
        dto.setModulo(notificacion.getModulo());
        return dto;
    }
}
