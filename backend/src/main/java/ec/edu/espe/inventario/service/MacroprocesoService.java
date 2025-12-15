package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.*;
import ec.edu.espe.inventario.model.entity.Macroproceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.repository.MacroprocesoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de Macroprocesos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MacroprocesoService {
    
    private final MacroprocesoRepository macroprocesoRepository;
    private final ProcesoService procesoService;
    
    /**
     * Genera un código único para el macroproceso
     */
    private String generarCodigo(String siglas) {
        String prefijo = siglas.toUpperCase();
        long count = macroprocesoRepository.count() + 1;
        String codigo;
        do {
            codigo = String.format("%s-MP-%03d", prefijo, count++);
        } while (macroprocesoRepository.existsByCodigo(codigo));
        return codigo;
    }
    
    /**
     * Crear un nuevo macroproceso
     */
    @Transactional
    public MacroprocesoResponseDTO crear(MacroprocesoRequestDTO request) {
        log.info("Creando nuevo macroproceso: {}", request.getNombre());
        
        Macroproceso macroproceso = new Macroproceso();
        macroproceso.setCodigo(generarCodigo(request.getUnidadEstrategica()));
        macroproceso.setTipo(request.getTipo());
        macroproceso.setNombre(request.getNombre());
        macroproceso.setDescripcion(request.getDescripcion());
        macroproceso.setUnidadEstrategica(request.getUnidadEstrategica());
        macroproceso.setResponsablePrincipal(request.getResponsablePrincipal());
        macroproceso.setObjetivosEstrategicos(request.getObjetivosEstrategicos());
        macroproceso.setEstadoDocumentacion(
            request.getEstadoDocumentacion() != null ? 
            request.getEstadoDocumentacion() : 
            EstadoDocumentacion.NO_DOCUMENTADO
        );
        macroproceso.setPorcentajeAvance(0);
        macroproceso.setCreadoPor("admin"); // Usuario hardcodeado para prototipo
        macroproceso.setActualizadoPor("admin");
        
        Macroproceso saved = macroprocesoRepository.save(macroproceso);
        log.info("Macroproceso creado con ID: {}", saved.getId());
        
        return convertirADTO(saved);
    }
    
    /**
     * Obtener todos los macroprocesos
     */
    @Transactional(readOnly = true)
    public List<MacroprocesoResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los macroprocesos");
        return macroprocesoRepository.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtener macroproceso por ID con sus procesos
     */
    @Transactional(readOnly = true)
    public MacroprocesoResponseDTO obtenerPorId(Long id) {
        log.info("Obteniendo macroproceso con ID: {}", id);
        Macroproceso macroproceso = macroprocesoRepository.findByIdWithProcesos(id)
            .orElseThrow(() -> new RuntimeException("Macroproceso no encontrado con ID: " + id));
        return convertirADTOConProcesos(macroproceso);
    }
    
    /**
     * Actualizar macroproceso
     */
    @Transactional
    public MacroprocesoResponseDTO actualizar(Long id, MacroprocesoRequestDTO request) {
        log.info("Actualizando macroproceso con ID: {}", id);
        
        Macroproceso macroproceso = macroprocesoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Macroproceso no encontrado con ID: " + id));
        
        macroproceso.setTipo(request.getTipo());
        macroproceso.setNombre(request.getNombre());
        macroproceso.setDescripcion(request.getDescripcion());
        macroproceso.setUnidadEstrategica(request.getUnidadEstrategica());
        macroproceso.setResponsablePrincipal(request.getResponsablePrincipal());
        macroproceso.setObjetivosEstrategicos(request.getObjetivosEstrategicos());
        if (request.getEstadoDocumentacion() != null) {
            macroproceso.setEstadoDocumentacion(request.getEstadoDocumentacion());
        }
        macroproceso.setActualizadoPor("admin");
        
        macroproceso.calcularPorcentajeAvance();
        
        Macroproceso updated = macroprocesoRepository.save(macroproceso);
        log.info("Macroproceso actualizado: {}", updated.getId());
        
        return convertirADTO(updated);
    }
    
    /**
     * Eliminar macroproceso (eliminación lógica)
     */
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando macroproceso con ID: {}", id);
        
        Macroproceso macroproceso = macroprocesoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Macroproceso no encontrado con ID: " + id));
        
        // Verificar que no tenga procesos activos
        if (!macroproceso.getProcesos().isEmpty()) {
            throw new RuntimeException("No se puede eliminar el macroproceso porque tiene procesos asociados");
        }
        
        macroprocesoRepository.delete(macroproceso);
        log.info("Macroproceso eliminado: {}", id);
    }
    
    /**
     * Actualizar estado de documentación
     */
    @Transactional
    public MacroprocesoResponseDTO actualizarEstado(Long id, EstadoDocumentacion nuevoEstado) {
        log.info("Actualizando estado del macroproceso {} a {}", id, nuevoEstado);
        
        Macroproceso macroproceso = macroprocesoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Macroproceso no encontrado con ID: " + id));
        
        macroproceso.setEstadoDocumentacion(nuevoEstado);
        macroproceso.calcularPorcentajeAvance();
        macroproceso.setActualizadoPor("admin");
        
        Macroproceso updated = macroprocesoRepository.save(macroproceso);
        return convertirADTO(updated);
    }
    
    /**
     * Convertir entidad a DTO simple
     */
    private MacroprocesoResponseDTO convertirADTO(Macroproceso macroproceso) {
        MacroprocesoResponseDTO dto = new MacroprocesoResponseDTO();
        dto.setId(macroproceso.getId());
        dto.setCodigo(macroproceso.getCodigo());
        dto.setTipo(macroproceso.getTipo());
        dto.setNombre(macroproceso.getNombre());
        dto.setDescripcion(macroproceso.getDescripcion());
        dto.setUnidadEstrategica(macroproceso.getUnidadEstrategica());
        dto.setResponsablePrincipal(macroproceso.getResponsablePrincipal());
        dto.setObjetivosEstrategicos(macroproceso.getObjetivosEstrategicos());
        dto.setEstadoDocumentacion(macroproceso.getEstadoDocumentacion());
        dto.setPorcentajeAvance(macroproceso.getPorcentajeAvance());
        dto.setFechaCreacion(macroproceso.getFechaCreacion());
        dto.setFechaActualizacion(macroproceso.getFechaActualizacion());
        dto.setCreadoPor(macroproceso.getCreadoPor());
        dto.setActualizadoPor(macroproceso.getActualizadoPor());
        dto.setCantidadProcesos(macroproceso.getProcesos() != null ? macroproceso.getProcesos().size() : 0);
        return dto;
    }
    
    /**
     * Convertir entidad a DTO con procesos
     */
    private MacroprocesoResponseDTO convertirADTOConProcesos(Macroproceso macroproceso) {
        MacroprocesoResponseDTO dto = convertirADTO(macroproceso);
        if (macroproceso.getProcesos() != null && !macroproceso.getProcesos().isEmpty()) {
            dto.setProcesos(macroproceso.getProcesos().stream()
                .map(procesoService::convertirADTOSimple)
                .collect(Collectors.toList()));
        }
        return dto;
    }
}
