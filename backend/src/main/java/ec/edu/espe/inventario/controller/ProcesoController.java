package ec.edu.espe.inventario.controller;

import ec.edu.espe.inventario.model.dto.ProcesoRequestDTO;
import ec.edu.espe.inventario.model.dto.ProcesoResponseDTO;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.service.ProcesoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Procesos
 */
@RestController
@RequestMapping("/api/procesos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ProcesoController {
    
    private final ProcesoService procesoService;
    
    /**
     * Crear un nuevo proceso
     * POST /api/procesos
     */
    @PostMapping
    public ResponseEntity<ProcesoResponseDTO> crear(@Valid @RequestBody ProcesoRequestDTO request) {
        log.info("POST /api/procesos - Crear proceso");
        ProcesoResponseDTO response = procesoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Obtener todos los procesos
     * GET /api/procesos
     */
    @GetMapping
    public ResponseEntity<List<ProcesoResponseDTO>> obtenerTodos() {
        log.info("GET /api/procesos - Obtener todos los procesos");
        List<ProcesoResponseDTO> procesos = procesoService.obtenerTodos();
        return ResponseEntity.ok(procesos);
    }
    
    /**
     * Obtener procesos por macroproceso
     * GET /api/procesos/macroproceso/{macroprocesoId}
     */
    @GetMapping("/macroproceso/{macroprocesoId}")
    public ResponseEntity<List<ProcesoResponseDTO>> obtenerPorMacroproceso(@PathVariable Long macroprocesoId) {
        log.info("GET /api/procesos/macroproceso/{} - Obtener procesos por macroproceso", macroprocesoId);
        List<ProcesoResponseDTO> procesos = procesoService.obtenerPorMacroproceso(macroprocesoId);
        return ResponseEntity.ok(procesos);
    }
    
    /**
     * Obtener un proceso por ID con sus subprocesos
     * GET /api/procesos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProcesoResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/procesos/{} - Obtener proceso por ID", id);
        ProcesoResponseDTO proceso = procesoService.obtenerPorId(id);
        return ResponseEntity.ok(proceso);
    }
    
    /**
     * Actualizar un proceso
     * PUT /api/procesos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProcesoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProcesoRequestDTO request) {
        log.info("PUT /api/procesos/{} - Actualizar proceso", id);
        ProcesoResponseDTO response = procesoService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Eliminar un proceso
     * DELETE /api/procesos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/procesos/{} - Eliminar proceso", id);
        procesoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Actualizar estado de documentación
     * PATCH /api/procesos/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ProcesoResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoDocumentacion nuevoEstado) {
        log.info("PATCH /api/procesos/{}/estado - Actualizar estado a {}", id, nuevoEstado);
        ProcesoResponseDTO response = procesoService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(response);
    }
}
