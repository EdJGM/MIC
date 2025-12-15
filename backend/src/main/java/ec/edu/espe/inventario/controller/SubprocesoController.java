package ec.edu.espe.inventario.controller;

import ec.edu.espe.inventario.model.dto.SubprocesoRequestDTO;
import ec.edu.espe.inventario.model.dto.SubprocesoResponseDTO;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.service.SubprocesoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Subprocesos
 */
@RestController
@RequestMapping("/api/subprocesos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class SubprocesoController {
    
    private final SubprocesoService subprocesoService;
    
    /**
     * Crear un nuevo subproceso
     * POST /api/subprocesos
     */
    @PostMapping
    public ResponseEntity<SubprocesoResponseDTO> crear(@Valid @RequestBody SubprocesoRequestDTO request) {
        log.info("POST /api/subprocesos - Crear subproceso");
        SubprocesoResponseDTO response = subprocesoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Obtener todos los subprocesos
     * GET /api/subprocesos
     */
    @GetMapping
    public ResponseEntity<List<SubprocesoResponseDTO>> obtenerTodos() {
        log.info("GET /api/subprocesos - Obtener todos los subprocesos");
        List<SubprocesoResponseDTO> subprocesos = subprocesoService.obtenerTodos();
        return ResponseEntity.ok(subprocesos);
    }
    
    /**
     * Obtener subprocesos por proceso
     * GET /api/subprocesos/proceso/{procesoId}
     */
    @GetMapping("/proceso/{procesoId}")
    public ResponseEntity<List<SubprocesoResponseDTO>> obtenerPorProceso(@PathVariable Long procesoId) {
        log.info("GET /api/subprocesos/proceso/{} - Obtener subprocesos por proceso", procesoId);
        List<SubprocesoResponseDTO> subprocesos = subprocesoService.obtenerPorProceso(procesoId);
        return ResponseEntity.ok(subprocesos);
    }
    
    /**
     * Obtener un subproceso por ID
     * GET /api/subprocesos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubprocesoResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/subprocesos/{} - Obtener subproceso por ID", id);
        SubprocesoResponseDTO subproceso = subprocesoService.obtenerPorId(id);
        return ResponseEntity.ok(subproceso);
    }
    
    /**
     * Actualizar un subproceso
     * PUT /api/subprocesos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubprocesoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SubprocesoRequestDTO request) {
        log.info("PUT /api/subprocesos/{} - Actualizar subproceso", id);
        SubprocesoResponseDTO response = subprocesoService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Eliminar un subproceso
     * DELETE /api/subprocesos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/subprocesos/{} - Eliminar subproceso", id);
        subprocesoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Actualizar estado de documentación
     * PATCH /api/subprocesos/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<SubprocesoResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoDocumentacion nuevoEstado) {
        log.info("PATCH /api/subprocesos/{}/estado - Actualizar estado a {}", id, nuevoEstado);
        SubprocesoResponseDTO response = subprocesoService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(response);
    }
}
