package ec.edu.espe.inventario.controller;

import ec.edu.espe.inventario.model.dto.ParametroSistemaRequestDTO;
import ec.edu.espe.inventario.model.dto.ParametroSistemaResponseDTO;
import ec.edu.espe.inventario.service.ParametroSistemaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de parámetros del sistema
 */
@RestController
@RequestMapping("/api/parametros")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ParametroSistemaController {

    private final ParametroSistemaService parametroSistemaService;

    /**
     * Obtener todos los parámetros del sistema
     * GET /api/parametros
     */
    @GetMapping
    public ResponseEntity<List<ParametroSistemaResponseDTO>> obtenerTodos() {
        log.info("GET /api/parametros - Obtener todos los parámetros");
        return ResponseEntity.ok(parametroSistemaService.obtenerTodos());
    }

    /**
     * Obtener parámetro por clave
     * GET /api/parametros/{clave}
     */
    @GetMapping("/{clave}")
    public ResponseEntity<ParametroSistemaResponseDTO> obtenerPorClave(@PathVariable String clave) {
        log.info("GET /api/parametros/{} - Obtener parámetro por clave", clave);
        return ResponseEntity.ok(parametroSistemaService.obtenerPorClave(clave));
    }

    /**
     * Actualizar valor de un parámetro
     * PUT /api/parametros/{clave}
     */
    @PutMapping("/{clave}")
    public ResponseEntity<ParametroSistemaResponseDTO> actualizar(
            @PathVariable String clave,
            @Valid @RequestBody ParametroSistemaRequestDTO request) {
        log.info("PUT /api/parametros/{} - Actualizar parámetro", clave);
        return ResponseEntity.ok(parametroSistemaService.actualizar(clave, request));
    }
}
