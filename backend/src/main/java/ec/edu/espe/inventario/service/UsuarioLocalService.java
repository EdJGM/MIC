package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.UsuarioLocalRequestDTO;
import ec.edu.espe.inventario.model.dto.UsuarioLocalResponseDTO;
import ec.edu.espe.inventario.model.entity.UsuarioLocal;
import ec.edu.espe.inventario.model.enums.AccionAudit;
import ec.edu.espe.inventario.model.enums.RolLocal;
import ec.edu.espe.inventario.repository.UsuarioLocalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de usuarios locales del sistema
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioLocalService {

    private final UsuarioLocalRepository usuarioLocalRepository;
    private final AuditLogService auditLogService;

    /**
     * Crear un nuevo usuario local (asignar rol a un usuario universitario)
     */
    @Transactional
    public UsuarioLocalResponseDTO crear(UsuarioLocalRequestDTO request) {
        log.info("Creando usuario local con externalId: {}", request.getExternalId());

        if (usuarioLocalRepository.existsByExternalId(request.getExternalId())) {
            throw new RuntimeException("Ya existe un usuario con el ID externo: " + request.getExternalId());
        }
        if (usuarioLocalRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + request.getEmail());
        }

        UsuarioLocal usuario = new UsuarioLocal();
        usuario.setExternalId(request.getExternalId());
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setRolLocal(request.getRolLocal());
        usuario.setUnidadAsignada(request.getUnidadAsignada());
        usuario.setActivo(true);
        usuario.setCreadoPor("admin"); // Temporal hasta integrar auth universitaria
        usuario.setActualizadoPor("admin");

        UsuarioLocal saved = usuarioLocalRepository.save(usuario);
        log.info("Usuario local creado con ID: {}", saved.getId());

        auditLogService.registrar("admin", "Admin Mock", AccionAudit.CREAR,
                "USUARIO_LOCAL", saved.getId(),
                "Usuario local creado: " + saved.getNombre() + " con rol " + saved.getRolLocal());

        return convertirADTO(saved);
    }

    /**
     * Obtener todos los usuarios locales
     */
    @Transactional(readOnly = true)
    public List<UsuarioLocalResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los usuarios locales");
        return usuarioLocalRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener usuarios por estado activo
     */
    @Transactional(readOnly = true)
    public List<UsuarioLocalResponseDTO> obtenerPorActivo(Boolean activo) {
        log.info("Obteniendo usuarios locales con activo={}", activo);
        return usuarioLocalRepository.findByActivo(activo).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener usuarios por rol
     */
    @Transactional(readOnly = true)
    public List<UsuarioLocalResponseDTO> obtenerPorRol(RolLocal rol) {
        log.info("Obteniendo usuarios locales con rol: {}", rol);
        return usuarioLocalRepository.findByRolLocal(rol).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener usuario por ID
     */
    @Transactional(readOnly = true)
    public UsuarioLocalResponseDTO obtenerPorId(Long id) {
        log.info("Obteniendo usuario local con ID: {}", id);
        UsuarioLocal usuario = usuarioLocalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario local no encontrado con ID: " + id));
        return convertirADTO(usuario);
    }

    /**
     * Actualizar rol y unidad de un usuario local
     */
    @Transactional
    public UsuarioLocalResponseDTO actualizar(Long id, UsuarioLocalRequestDTO request) {
        log.info("Actualizando usuario local con ID: {}", id);

        UsuarioLocal usuario = usuarioLocalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario local no encontrado con ID: " + id));

        // Verificar email único si cambió
        if (!usuario.getEmail().equals(request.getEmail())
                && usuarioLocalRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + request.getEmail());
        }

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setRolLocal(request.getRolLocal());
        usuario.setUnidadAsignada(request.getUnidadAsignada());
        usuario.setActualizadoPor("admin");

        UsuarioLocal updated = usuarioLocalRepository.save(usuario);
        log.info("Usuario local actualizado: {}", updated.getId());

        auditLogService.registrar("admin", "Admin Mock", AccionAudit.ACTUALIZAR,
                "USUARIO_LOCAL", updated.getId(),
                "Usuario local actualizado: " + updated.getNombre());

        return convertirADTO(updated);
    }

    /**
     * Desactivar usuario (eliminación lógica)
     */
    @Transactional
    public UsuarioLocalResponseDTO desactivar(Long id) {
        log.info("Desactivando usuario local con ID: {}", id);

        UsuarioLocal usuario = usuarioLocalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario local no encontrado con ID: " + id));

        if (!usuario.getActivo()) {
            throw new RuntimeException("El usuario ya se encuentra desactivado");
        }

        usuario.setActivo(false);
        usuario.setActualizadoPor("admin");

        UsuarioLocal updated = usuarioLocalRepository.save(usuario);
        log.info("Usuario local desactivado: {}", updated.getId());

        auditLogService.registrar("admin", "Admin Mock", AccionAudit.ELIMINAR,
                "USUARIO_LOCAL", updated.getId(),
                "Usuario local desactivado: " + updated.getNombre());

        return convertirADTO(updated);
    }

    /**
     * Reactivar usuario
     */
    @Transactional
    public UsuarioLocalResponseDTO activar(Long id) {
        log.info("Activando usuario local con ID: {}", id);

        UsuarioLocal usuario = usuarioLocalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario local no encontrado con ID: " + id));

        if (usuario.getActivo()) {
            throw new RuntimeException("El usuario ya se encuentra activo");
        }

        usuario.setActivo(true);
        usuario.setActualizadoPor("admin");

        UsuarioLocal updated = usuarioLocalRepository.save(usuario);
        log.info("Usuario local activado: {}", updated.getId());

        auditLogService.registrar("admin", "Admin Mock", AccionAudit.CAMBIO_ESTADO,
                "USUARIO_LOCAL", updated.getId(),
                "Usuario local activado: " + updated.getNombre());

        return convertirADTO(updated);
    }

    /**
     * Convertir entidad a DTO
     */
    private UsuarioLocalResponseDTO convertirADTO(UsuarioLocal usuario) {
        UsuarioLocalResponseDTO dto = new UsuarioLocalResponseDTO();
        dto.setId(usuario.getId());
        dto.setExternalId(usuario.getExternalId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setRolLocal(usuario.getRolLocal());
        dto.setRolNombre(usuario.getRolLocal().getNombre());
        dto.setUnidadAsignada(usuario.getUnidadAsignada());
        dto.setActivo(usuario.getActivo());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        dto.setFechaActualizacion(usuario.getFechaActualizacion());
        dto.setCreadoPor(usuario.getCreadoPor());
        dto.setActualizadoPor(usuario.getActualizadoPor());
        return dto;
    }
}
