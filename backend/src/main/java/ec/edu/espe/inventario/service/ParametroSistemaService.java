package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.ParametroSistemaRequestDTO;
import ec.edu.espe.inventario.model.dto.ParametroSistemaResponseDTO;
import ec.edu.espe.inventario.model.entity.ParametroSistema;
import ec.edu.espe.inventario.model.enums.AccionAudit;
import ec.edu.espe.inventario.repository.ParametroSistemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de parámetros del sistema según ERS (RF-35)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ParametroSistemaService {

    private final ParametroSistemaRepository parametroSistemaRepository;
    private final AuditLogService auditLogService;

    /**
     * Obtener todos los parámetros
     */
    @Transactional(readOnly = true)
    public List<ParametroSistemaResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los parámetros del sistema");
        return parametroSistemaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener un parámetro por su clave
     */
    @Transactional(readOnly = true)
    public ParametroSistemaResponseDTO obtenerPorClave(String clave) {
        log.info("Obteniendo parámetro con clave: {}", clave);
        ParametroSistema parametro = parametroSistemaRepository.findByClave(clave)
                .orElseThrow(() -> new RuntimeException("Parámetro no encontrado con clave: " + clave));
        return convertirADTO(parametro);
    }

    /**
     * Actualizar el valor de un parámetro
     */
    @Transactional
    public ParametroSistemaResponseDTO actualizar(String clave, ParametroSistemaRequestDTO request) {
        log.info("Actualizando parámetro con clave: {}", clave);

        ParametroSistema parametro = parametroSistemaRepository.findByClave(clave)
                .orElseThrow(() -> new RuntimeException("Parámetro no encontrado con clave: " + clave));

        String valorAnterior = parametro.getValor();
        parametro.setValor(request.getValor());
        parametro.setModificadoPor("admin"); // Temporal hasta integrar auth universitaria

        ParametroSistema updated = parametroSistemaRepository.save(parametro);
        log.info("Parámetro {} actualizado: {} -> {}", clave, valorAnterior, request.getValor());

        auditLogService.registrar("admin", "Admin Mock", AccionAudit.ACTUALIZAR,
                "PARAMETRO", updated.getId(),
                "Parámetro '" + clave + "' actualizado de '" + valorAnterior + "' a '" + request.getValor() + "'");

        return convertirADTO(updated);
    }

    /**
     * Obtener el valor numérico de un parámetro
     */
    @Transactional(readOnly = true)
    public Integer obtenerValorNumerico(String clave) {
        return parametroSistemaRepository.findByClave(clave)
                .map(p -> {
                    try {
                        return Integer.parseInt(p.getValor());
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("El parámetro '" + clave + "' no tiene un valor numérico válido");
                    }
                })
                .orElseThrow(() -> new RuntimeException("Parámetro no encontrado: " + clave));
    }

    /**
     * Convertir entidad a DTO
     */
    private ParametroSistemaResponseDTO convertirADTO(ParametroSistema parametro) {
        ParametroSistemaResponseDTO dto = new ParametroSistemaResponseDTO();
        dto.setId(parametro.getId());
        dto.setClave(parametro.getClave());
        dto.setValor(parametro.getValor());
        dto.setDescripcion(parametro.getDescripcion());
        dto.setTipo(parametro.getTipo());
        dto.setFechaActualizacion(parametro.getFechaActualizacion());
        dto.setModificadoPor(parametro.getModificadoPor());
        return dto;
    }
}
