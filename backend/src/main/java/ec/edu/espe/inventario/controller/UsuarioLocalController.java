package ec.edu.espe.inventario.controller;

import ec.edu.espe.inventario.model.dto.UsuarioLocalRequestDTO;
import ec.edu.espe.inventario.model.dto.UsuarioLocalResponseDTO;
import ec.edu.espe.inventario.model.enums.RolLocal;
import ec.edu.espe.inventario.service.UsuarioLocalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de usuarios locales del sistema
 */
@RestController
@RequestMapping("/api/usuarios-locales")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class UsuarioLocalController {

    private final UsuarioLocalService usuarioLocalService;

    /**
     * Crear usuario local (asignar rol a usuario universitario)
     * POST /api/usuarios-locales
     */
    @PostMapping
    public ResponseEntity<UsuarioLocalResponseDTO> crear(@Valid @RequestBody UsuarioLocalRequestDTO request) {
        log.info("POST /api/usuarios-locales - Crear usuario local");
        UsuarioLocalResponseDTO response = usuarioLocalService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtener todos los usuarios locales
     * GET /api/usuarios-locales
     * Parámetros opcionales: activo, rol
     */
    @GetMapping
    public ResponseEntity<List<UsuarioLocalResponseDTO>> obtenerTodos(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) RolLocal rol) {
        log.info("GET /api/usuarios-locales - activo={}, rol={}", activo, rol);

        if (activo != null) {
            return ResponseEntity.ok(usuarioLocalService.obtenerPorActivo(activo));
        }
        if (rol != null) {
            return ResponseEntity.ok(usuarioLocalService.obtenerPorRol(rol));
        }
        return ResponseEntity.ok(usuarioLocalService.obtenerTodos());
    }

    /**
     * Obtener usuario local por ID
     * GET /api/usuarios-locales/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioLocalResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/usuarios-locales/{} - Obtener usuario local por ID", id);
        return ResponseEntity.ok(usuarioLocalService.obtenerPorId(id));
    }

    /**
     * Actualizar usuario local (cambiar rol, unidad, datos)
     * PUT /api/usuarios-locales/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioLocalResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioLocalRequestDTO request) {
        log.info("PUT /api/usuarios-locales/{} - Actualizar usuario local", id);
        return ResponseEntity.ok(usuarioLocalService.actualizar(id, request));
    }

    /**
     * Desactivar usuario local
     * PATCH /api/usuarios-locales/{id}/desactivar
     */
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioLocalResponseDTO> desactivar(@PathVariable Long id) {
        log.info("PATCH /api/usuarios-locales/{}/desactivar", id);
        return ResponseEntity.ok(usuarioLocalService.desactivar(id));
    }

    /**
     * Activar usuario local
     * PATCH /api/usuarios-locales/{id}/activar
     */
    @PatchMapping("/{id}/activar")
    public ResponseEntity<UsuarioLocalResponseDTO> activar(@PathVariable Long id) {
        log.info("PATCH /api/usuarios-locales/{}/activar", id);
        return ResponseEntity.ok(usuarioLocalService.activar(id));
    }
}
