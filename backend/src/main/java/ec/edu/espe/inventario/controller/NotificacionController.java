package ec.edu.espe.inventario.controller;

import ec.edu.espe.inventario.model.dto.NotificacionResponseDTO;
import ec.edu.espe.inventario.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de notificaciones del sistema
 */
@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class NotificacionController {

    private final NotificacionService notificacionService;

    /**
     * Obtener todas las notificaciones de un usuario
     * GET /api/notificaciones?destinatarioId=
     */
    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerPorUsuario(
            @RequestParam String destinatarioId) {
        log.info("GET /api/notificaciones - destinatarioId={}", destinatarioId);
        return ResponseEntity.ok(notificacionService.obtenerPorUsuario(destinatarioId));
    }

    /**
     * Obtener notificaciones no leídas de un usuario
     * GET /api/notificaciones/no-leidas?destinatarioId=
     */
    @GetMapping("/no-leidas")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerNoLeidas(
            @RequestParam String destinatarioId) {
        log.info("GET /api/notificaciones/no-leidas - destinatarioId={}", destinatarioId);
        return ResponseEntity.ok(notificacionService.obtenerNoLeidas(destinatarioId));
    }

    /**
     * Contar notificaciones no leídas de un usuario
     * GET /api/notificaciones/contador?destinatarioId=
     */
    @GetMapping("/contador")
    public ResponseEntity<Map<String, Long>> contarNoLeidas(
            @RequestParam String destinatarioId) {
        log.info("GET /api/notificaciones/contador - destinatarioId={}", destinatarioId);
        long count = notificacionService.contarNoLeidas(destinatarioId);
        return ResponseEntity.ok(Map.of("noLeidas", count));
    }

    /**
     * Marcar una notificación como leída
     * PATCH /api/notificaciones/{id}/leida
     */
    @PatchMapping("/{id}/leida")
    public ResponseEntity<NotificacionResponseDTO> marcarComoLeida(@PathVariable Long id) {
        log.info("PATCH /api/notificaciones/{}/leida", id);
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }

    /**
     * Marcar todas las notificaciones de un usuario como leídas
     * PATCH /api/notificaciones/marcar-todas-leidas?destinatarioId=
     */
    @PatchMapping("/marcar-todas-leidas")
    public ResponseEntity<Void> marcarTodasComoLeidas(@RequestParam String destinatarioId) {
        log.info("PATCH /api/notificaciones/marcar-todas-leidas - destinatarioId={}", destinatarioId);
        notificacionService.marcarTodasComoLeidas(destinatarioId);
        return ResponseEntity.noContent().build();
    }
}
