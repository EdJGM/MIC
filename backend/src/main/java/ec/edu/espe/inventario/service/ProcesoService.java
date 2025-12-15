package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.*;
import ec.edu.espe.inventario.model.entity.Macroproceso;
import ec.edu.espe.inventario.model.entity.Proceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.repository.MacroprocesoRepository;
import ec.edu.espe.inventario.repository.ProcesoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de Procesos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcesoService {
    
    private final ProcesoRepository procesoRepository;
    private final MacroprocesoRepository macroprocesoRepository;
    private final SubprocesoService subprocesoService;
    
    /**
     * Genera un código único para el proceso
     */
    private String generarCodigo(String codigoMacroproceso) {
        long count = procesoRepository.count() + 1;
        String codigo;
        do {
            codigo = String.format("%s-P-%03d", codigoMacroproceso, count++);
        } while (procesoRepository.existsByCodigo(codigo));
        return codigo;
    }
    
    /**
     * Crear un nuevo proceso
     */
    @Transactional
    public ProcesoResponseDTO crear(ProcesoRequestDTO request) {
        log.info("Creando nuevo proceso: {}", request.getNombre());
        
        Macroproceso macroproceso = macroprocesoRepository.findById(request.getMacroprocesoId())
            .orElseThrow(() -> new RuntimeException("Macroproceso no encontrado con ID: " + request.getMacroprocesoId()));
        
        Proceso proceso = new Proceso();
        proceso.setCodigo(generarCodigo(macroproceso.getCodigo()));
        proceso.setNombre(request.getNombre());
        proceso.setDescripcion(request.getDescripcion());
        proceso.setObjetivos(request.getObjetivos());
        proceso.setMacroproceso(macroproceso);
        proceso.setEstadoDocumentacion(
            request.getEstadoDocumentacion() != null ? 
            request.getEstadoDocumentacion() : 
            EstadoDocumentacion.NO_DOCUMENTADO
        );
        proceso.setPorcentajeAvance(0);
        proceso.setCreadoPor("admin");
        proceso.setActualizadoPor("admin");
        
        Proceso saved = procesoRepository.save(proceso);
        
        // Actualizar el porcentaje del macroproceso
        macroproceso.calcularPorcentajeAvance();
        macroprocesoRepository.save(macroproceso);
        
        log.info("Proceso creado con ID: {}", saved.getId());
        return convertirADTOSimple(saved);
    }
    
    /**
     * Obtener todos los procesos
     */
    @Transactional(readOnly = true)
    public List<ProcesoResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los procesos");
        return procesoRepository.findAll().stream()
            .map(this::convertirADTOSimple)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtener procesos por macroproceso
     */
    @Transactional(readOnly = true)
    public List<ProcesoResponseDTO> obtenerPorMacroproceso(Long macroprocesoId) {
        log.info("Obteniendo procesos del macroproceso: {}", macroprocesoId);
        return procesoRepository.findByMacroprocesoId(macroprocesoId).stream()
            .map(this::convertirADTOSimple)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtener proceso por ID con sus subprocesos
     */
    @Transactional(readOnly = true)
    public ProcesoResponseDTO obtenerPorId(Long id) {
        log.info("Obteniendo proceso con ID: {}", id);
        Proceso proceso = procesoRepository.findByIdWithSubprocesos(id)
            .orElseThrow(() -> new RuntimeException("Proceso no encontrado con ID: " + id));
        return convertirADTOConSubprocesos(proceso);
    }
    
    /**
     * Actualizar proceso
     */
    @Transactional
    public ProcesoResponseDTO actualizar(Long id, ProcesoRequestDTO request) {
        log.info("Actualizando proceso con ID: {}", id);
        
        Proceso proceso = procesoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Proceso no encontrado con ID: " + id));
        
        proceso.setNombre(request.getNombre());
        proceso.setDescripcion(request.getDescripcion());
        proceso.setObjetivos(request.getObjetivos());
        if (request.getEstadoDocumentacion() != null) {
            proceso.setEstadoDocumentacion(request.getEstadoDocumentacion());
        }
        proceso.setActualizadoPor("admin");
        
        proceso.calcularPorcentajeAvance();
        Proceso updated = procesoRepository.save(proceso);
        
        // Actualizar el macroproceso padre
        updated.getMacroproceso().calcularPorcentajeAvance();
        macroprocesoRepository.save(updated.getMacroproceso());
        
        log.info("Proceso actualizado: {}", updated.getId());
        return convertirADTOSimple(updated);
    }
    
    /**
     * Eliminar proceso
     */
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando proceso con ID: {}", id);
        
        Proceso proceso = procesoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Proceso no encontrado con ID: " + id));
        
        // Verificar que no tenga subprocesos activos
        if (!proceso.getSubprocesos().isEmpty()) {
            throw new RuntimeException("No se puede eliminar el proceso porque tiene subprocesos asociados");
        }
        
        Macroproceso macroproceso = proceso.getMacroproceso();
        procesoRepository.delete(proceso);
        
        // Actualizar el macroproceso padre
        macroproceso.calcularPorcentajeAvance();
        macroprocesoRepository.save(macroproceso);
        
        log.info("Proceso eliminado: {}", id);
    }
    
    /**
     * Actualizar estado de documentación
     */
    @Transactional
    public ProcesoResponseDTO actualizarEstado(Long id, EstadoDocumentacion nuevoEstado) {
        log.info("Actualizando estado del proceso {} a {}", id, nuevoEstado);
        
        Proceso proceso = procesoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Proceso no encontrado con ID: " + id));
        
        proceso.setEstadoDocumentacion(nuevoEstado);
        proceso.calcularPorcentajeAvance();
        proceso.setActualizadoPor("admin");
        
        Proceso updated = procesoRepository.save(proceso);
        
        // Actualizar el macroproceso padre
        updated.getMacroproceso().calcularPorcentajeAvance();
        macroprocesoRepository.save(updated.getMacroproceso());
        
        return convertirADTOSimple(updated);
    }
    
    /**
     * Convertir entidad a DTO simple (sin relaciones)
     */
    public ProcesoResponseDTO convertirADTOSimple(Proceso proceso) {
        ProcesoResponseDTO dto = new ProcesoResponseDTO();
        dto.setId(proceso.getId());
        dto.setCodigo(proceso.getCodigo());
        dto.setNombre(proceso.getNombre());
        dto.setDescripcion(proceso.getDescripcion());
        dto.setObjetivos(proceso.getObjetivos());
        dto.setMacroprocesoId(proceso.getMacroproceso().getId());
        dto.setMacroprocesoNombre(proceso.getMacroproceso().getNombre());
        dto.setEstadoDocumentacion(proceso.getEstadoDocumentacion());
        dto.setPorcentajeAvance(proceso.getPorcentajeAvance());
        dto.setFechaCreacion(proceso.getFechaCreacion());
        dto.setFechaActualizacion(proceso.getFechaActualizacion());
        dto.setCreadoPor(proceso.getCreadoPor());
        dto.setActualizadoPor(proceso.getActualizadoPor());
        dto.setCantidadSubprocesos(proceso.getSubprocesos() != null ? proceso.getSubprocesos().size() : 0);
        return dto;
    }
    
    /**
     * Convertir entidad a DTO con subprocesos
     */
    private ProcesoResponseDTO convertirADTOConSubprocesos(Proceso proceso) {
        ProcesoResponseDTO dto = convertirADTOSimple(proceso);
        if (proceso.getSubprocesos() != null && !proceso.getSubprocesos().isEmpty()) {
            dto.setSubprocesos(proceso.getSubprocesos().stream()
                .map(subprocesoService::convertirADTO)
                .collect(Collectors.toList()));
        }
        return dto;
    }
}
