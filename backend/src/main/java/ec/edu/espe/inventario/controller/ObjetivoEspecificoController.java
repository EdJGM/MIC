package ec.edu.espe.inventario.controller;

import ec.edu.espe.inventario.model.dto.ObjetivoEspecificoRequestDTO;
import ec.edu.espe.inventario.model.dto.ObjetivoEspecificoResponseDTO;
import ec.edu.espe.inventario.service.ObjetivoEspecificoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Objetivos Especificos
 */
@RestController
@RequestMapping("/api/objetivos-especificos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ObjetivoEspecificoController {

    private final ObjetivoEspecificoService objetivoEspecificoService;

    /**
     * Crear un nuevo objetivo especifico
     * POST /api/objetivos-especificos
     */
    @PostMapping
    public ResponseEntity<ObjetivoEspecificoResponseDTO> crear(@Valid @RequestBody ObjetivoEspecificoRequestDTO request) {
        log.info("POST /api/objetivos-especificos - Crear objetivo especifico");
        ObjetivoEspecificoResponseDTO response = objetivoEspecificoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtener todos los objetivos especificos
     * GET /api/objetivos-especificos
     */
    @GetMapping
    public ResponseEntity<List<ObjetivoEspecificoResponseDTO>> obtenerTodos() {
        log.info("GET /api/objetivos-especificos - Obtener todos los objetivos especificos");
        List<ObjetivoEspecificoResponseDTO> objetivos = objetivoEspecificoService.obtenerTodos();
        return ResponseEntity.ok(objetivos);
    }

    /**
     * Obtener un objetivo especifico por ID
     * GET /api/objetivos-especificos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ObjetivoEspecificoResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/objetivos-especificos/{} - Obtener objetivo especifico por ID", id);
        ObjetivoEspecificoResponseDTO objetivo = objetivoEspecificoService.obtenerPorId(id);
        return ResponseEntity.ok(objetivo);
    }

    /**
     * Actualizar un objetivo especifico
     * PUT /api/objetivos-especificos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ObjetivoEspecificoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ObjetivoEspecificoRequestDTO request) {
        log.info("PUT /api/objetivos-especificos/{} - Actualizar objetivo especifico", id);
        ObjetivoEspecificoResponseDTO response = objetivoEspecificoService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Eliminar un objetivo especifico
     * DELETE /api/objetivos-especificos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/objetivos-especificos/{} - Eliminar objetivo especifico", id);
        objetivoEspecificoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
