package ec.edu.espe.inventario.controller;

import ec.edu.espe.inventario.model.dto.MacroprocesoRequestDTO;
import ec.edu.espe.inventario.model.dto.MacroprocesoResponseDTO;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.service.MacroprocesoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Macroprocesos
 */
@RestController
@RequestMapping("/api/macroprocesos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class MacroprocesoController {
    
    private final MacroprocesoService macroprocesoService;
    
    /**
     * Crear un nuevo macroproceso
     * POST /api/macroprocesos
     */
    @PostMapping
    public ResponseEntity<MacroprocesoResponseDTO> crear(@Valid @RequestBody MacroprocesoRequestDTO request) {
        log.info("POST /api/macroprocesos - Crear macroproceso");
        MacroprocesoResponseDTO response = macroprocesoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Obtener todos los macroprocesos
     * GET /api/macroprocesos
     */
    @GetMapping
    public ResponseEntity<List<MacroprocesoResponseDTO>> obtenerTodos() {
        log.info("GET /api/macroprocesos - Obtener todos los macroprocesos");
        List<MacroprocesoResponseDTO> macroprocesos = macroprocesoService.obtenerTodos();
        return ResponseEntity.ok(macroprocesos);
    }
    
    /**
     * Obtener un macroproceso por ID con sus procesos
     * GET /api/macroprocesos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<MacroprocesoResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/macroprocesos/{} - Obtener macroproceso por ID", id);
        MacroprocesoResponseDTO macroproceso = macroprocesoService.obtenerPorId(id);
        return ResponseEntity.ok(macroproceso);
    }
    
    /**
     * Actualizar un macroproceso
     * PUT /api/macroprocesos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<MacroprocesoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MacroprocesoRequestDTO request) {
        log.info("PUT /api/macroprocesos/{} - Actualizar macroproceso", id);
        MacroprocesoResponseDTO response = macroprocesoService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Eliminar un macroproceso
     * DELETE /api/macroprocesos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/macroprocesos/{} - Eliminar macroproceso", id);
        macroprocesoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Actualizar estado de documentación
     * PATCH /api/macroprocesos/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<MacroprocesoResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoDocumentacion nuevoEstado) {
        log.info("PATCH /api/macroprocesos/{}/estado - Actualizar estado a {}", id, nuevoEstado);
        MacroprocesoResponseDTO response = macroprocesoService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(response);
    }
}
