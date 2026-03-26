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
 * Controlador REST para Subprocesos (SP-N1 y SP-N2)
 */
@RestController
@RequestMapping("/api/subprocesos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class SubprocesoController {

    private final SubprocesoService subprocesoService;

    /** POST /api/subprocesos */
    @PostMapping
    public ResponseEntity<SubprocesoResponseDTO> crear(@Valid @RequestBody SubprocesoRequestDTO request) {
        log.info("POST /api/subprocesos - Crear subproceso SP-N{}", request.getNivel());
        return ResponseEntity.status(HttpStatus.CREATED).body(subprocesoService.crear(request));
    }

    /** GET /api/subprocesos */
    @GetMapping
    public ResponseEntity<List<SubprocesoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(subprocesoService.obtenerTodos());
    }

    /** GET /api/subprocesos/proceso/{procesoId} */
    @GetMapping("/proceso/{procesoId}")
    public ResponseEntity<List<SubprocesoResponseDTO>> obtenerPorProceso(@PathVariable Long procesoId) {
        return ResponseEntity.ok(subprocesoService.obtenerPorProceso(procesoId));
    }

    /**
     * GET /api/subprocesos/proceso/{procesoId}/nivel/{nivel}
     * Filtra SP-N1 o SP-N2 de un Proceso N2
     */
    @GetMapping("/proceso/{procesoId}/nivel/{nivel}")
    public ResponseEntity<List<SubprocesoResponseDTO>> obtenerPorProcesoYNivel(
            @PathVariable Long procesoId,
            @PathVariable int nivel) {
        return ResponseEntity.ok(subprocesoService.obtenerPorProcesoYNivel(procesoId, nivel));
    }

    /**
     * GET /api/subprocesos/padre/{subprocesoPadreId}
     * Obtiene los SP-N2 hijos de un SP-N1
     */
    @GetMapping("/padre/{subprocesoPadreId}")
    public ResponseEntity<List<SubprocesoResponseDTO>> obtenerPorSubprocesoPadre(
            @PathVariable Long subprocesoPadreId) {
        return ResponseEntity.ok(subprocesoService.obtenerPorSubprocesoPadre(subprocesoPadreId));
    }

    /** GET /api/subprocesos/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<SubprocesoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(subprocesoService.obtenerPorId(id));
    }

    /** PUT /api/subprocesos/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<SubprocesoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SubprocesoRequestDTO request) {
        return ResponseEntity.ok(subprocesoService.actualizar(id, request));
    }

    /** DELETE /api/subprocesos/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        subprocesoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /** PATCH /api/subprocesos/{id}/estado */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<SubprocesoResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoDocumentacion nuevoEstado) {
        return ResponseEntity.ok(subprocesoService.actualizarEstado(id, nuevoEstado));
    }
}
