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
 * Controlador REST para Procesos (N1 y N2)
 */
@RestController
@RequestMapping("/api/procesos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ProcesoController {

    private final ProcesoService procesoService;

    /** POST /api/procesos */
    @PostMapping
    public ResponseEntity<ProcesoResponseDTO> crear(@Valid @RequestBody ProcesoRequestDTO request) {
        log.info("POST /api/procesos - Crear proceso N{}", request.getNivel());
        return ResponseEntity.status(HttpStatus.CREATED).body(procesoService.crear(request));
    }

    /** GET /api/procesos */
    @GetMapping
    public ResponseEntity<List<ProcesoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(procesoService.obtenerTodos());
    }

    /** GET /api/procesos/macroproceso/{macroprocesoId} */
    @GetMapping("/macroproceso/{macroprocesoId}")
    public ResponseEntity<List<ProcesoResponseDTO>> obtenerPorMacroproceso(@PathVariable Long macroprocesoId) {
        return ResponseEntity.ok(procesoService.obtenerPorMacroproceso(macroprocesoId));
    }

    /**
     * GET /api/procesos/macroproceso/{macroprocesoId}/nivel/{nivel}
     * Filtra Procesos N1 o N2 de un Macroproceso
     */
    @GetMapping("/macroproceso/{macroprocesoId}/nivel/{nivel}")
    public ResponseEntity<List<ProcesoResponseDTO>> obtenerPorMacroprocesoYNivel(
            @PathVariable Long macroprocesoId,
            @PathVariable int nivel) {
        return ResponseEntity.ok(procesoService.obtenerPorMacroprocesoYNivel(macroprocesoId, nivel));
    }

    /**
     * GET /api/procesos/padre/{procesoPadreId}
     * Obtiene los Procesos N2 hijos de un Proceso N1
     */
    @GetMapping("/padre/{procesoPadreId}")
    public ResponseEntity<List<ProcesoResponseDTO>> obtenerPorProcesoPadre(@PathVariable Long procesoPadreId) {
        return ResponseEntity.ok(procesoService.obtenerPorProcesoPadre(procesoPadreId));
    }

    /** GET /api/procesos/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ProcesoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(procesoService.obtenerPorId(id));
    }

    /** PUT /api/procesos/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ProcesoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProcesoRequestDTO request) {
        return ResponseEntity.ok(procesoService.actualizar(id, request));
    }

    /** DELETE /api/procesos/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        procesoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /** PATCH /api/procesos/{id}/estado */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ProcesoResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoDocumentacion nuevoEstado) {
        return ResponseEntity.ok(procesoService.actualizarEstado(id, nuevoEstado));
    }
}
