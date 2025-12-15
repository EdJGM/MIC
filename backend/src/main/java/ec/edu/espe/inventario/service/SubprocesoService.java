package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.SubprocesoRequestDTO;
import ec.edu.espe.inventario.model.dto.SubprocesoResponseDTO;
import ec.edu.espe.inventario.model.entity.Proceso;
import ec.edu.espe.inventario.model.entity.Subproceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.repository.MacroprocesoRepository;
import ec.edu.espe.inventario.repository.ProcesoRepository;
import ec.edu.espe.inventario.repository.SubprocesoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de Subprocesos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubprocesoService {
    
    private final SubprocesoRepository subprocesoRepository;
    private final ProcesoRepository procesoRepository;
    private final MacroprocesoRepository macroprocesoRepository;
    
    /**
     * Genera un código único para el subproceso
     */
    private String generarCodigo(String codigoProceso) {
        long count = subprocesoRepository.count() + 1;
        String codigo;
        do {
            codigo = String.format("%s-SP-%03d", codigoProceso, count++);
        } while (subprocesoRepository.existsByCodigo(codigo));
        return codigo;
    }
    
    /**
     * Crear un nuevo subproceso
     */
    @Transactional
    public SubprocesoResponseDTO crear(SubprocesoRequestDTO request) {
        log.info("Creando nuevo subproceso: {}", request.getNombre());
        
        Proceso proceso = procesoRepository.findById(request.getProcesoId())
            .orElseThrow(() -> new RuntimeException("Proceso no encontrado con ID: " + request.getProcesoId()));
        
        Subproceso subproceso = new Subproceso();
        subproceso.setCodigo(generarCodigo(proceso.getCodigo()));
        subproceso.setNombre(request.getNombre());
        subproceso.setDescripcion(request.getDescripcion());
        subproceso.setProceso(proceso);
        subproceso.setEstadoDocumentacion(
            request.getEstadoDocumentacion() != null ? 
            request.getEstadoDocumentacion() : 
            EstadoDocumentacion.NO_DOCUMENTADO
        );
        subproceso.setCreadoPor("admin");
        subproceso.setActualizadoPor("admin");
        
        Subproceso saved = subprocesoRepository.save(subproceso);
        
        // Actualizar el proceso padre y el macroproceso abuelo
        proceso.calcularPorcentajeAvance();
        procesoRepository.save(proceso);
        
        proceso.getMacroproceso().calcularPorcentajeAvance();
        macroprocesoRepository.save(proceso.getMacroproceso());
        
        log.info("Subproceso creado con ID: {}", saved.getId());
        return convertirADTO(saved);
    }
    
    /**
     * Obtener todos los subprocesos
     */
    @Transactional(readOnly = true)
    public List<SubprocesoResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los subprocesos");
        return subprocesoRepository.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtener subprocesos por proceso
     */
    @Transactional(readOnly = true)
    public List<SubprocesoResponseDTO> obtenerPorProceso(Long procesoId) {
        log.info("Obteniendo subprocesos del proceso: {}", procesoId);
        return subprocesoRepository.findByProcesoId(procesoId).stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtener subproceso por ID
     */
    @Transactional(readOnly = true)
    public SubprocesoResponseDTO obtenerPorId(Long id) {
        log.info("Obteniendo subproceso con ID: {}", id);
        Subproceso subproceso = subprocesoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subproceso no encontrado con ID: " + id));
        return convertirADTO(subproceso);
    }
    
    /**
     * Actualizar subproceso
     */
    @Transactional
    public SubprocesoResponseDTO actualizar(Long id, SubprocesoRequestDTO request) {
        log.info("Actualizando subproceso con ID: {}", id);
        
        Subproceso subproceso = subprocesoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subproceso no encontrado con ID: " + id));
        
        subproceso.setNombre(request.getNombre());
        subproceso.setDescripcion(request.getDescripcion());
        if (request.getEstadoDocumentacion() != null) {
            subproceso.setEstadoDocumentacion(request.getEstadoDocumentacion());
            subproceso.setPorcentajeAvance(request.getEstadoDocumentacion().getPorcentajeAsociado());
        }
        subproceso.setActualizadoPor("admin");
        
        Subproceso updated = subprocesoRepository.save(subproceso);
        
        // Actualizar el proceso padre y el macroproceso abuelo
        Proceso proceso = updated.getProceso();
        proceso.calcularPorcentajeAvance();
        procesoRepository.save(proceso);
        
        proceso.getMacroproceso().calcularPorcentajeAvance();
        macroprocesoRepository.save(proceso.getMacroproceso());
        
        log.info("Subproceso actualizado: {}", updated.getId());
        return convertirADTO(updated);
    }
    
    /**
     * Eliminar subproceso
     */
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando subproceso con ID: {}", id);
        
        Subproceso subproceso = subprocesoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subproceso no encontrado con ID: " + id));
        
        Proceso proceso = subproceso.getProceso();
        subprocesoRepository.delete(subproceso);
        
        // Actualizar el proceso padre y el macroproceso abuelo
        proceso.calcularPorcentajeAvance();
        procesoRepository.save(proceso);
        
        proceso.getMacroproceso().calcularPorcentajeAvance();
        macroprocesoRepository.save(proceso.getMacroproceso());
        
        log.info("Subproceso eliminado: {}", id);
    }
    
    /**
     * Actualizar estado de documentación
     */
    @Transactional
    public SubprocesoResponseDTO actualizarEstado(Long id, EstadoDocumentacion nuevoEstado) {
        log.info("Actualizando estado del subproceso {} a {}", id, nuevoEstado);
        
        Subproceso subproceso = subprocesoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subproceso no encontrado con ID: " + id));
        
        subproceso.setEstadoDocumentacion(nuevoEstado);
        subproceso.setPorcentajeAvance(nuevoEstado.getPorcentajeAsociado());
        subproceso.setActualizadoPor("admin");
        
        Subproceso updated = subprocesoRepository.save(subproceso);
        
        // Actualizar el proceso padre y el macroproceso abuelo
        Proceso proceso = updated.getProceso();
        proceso.calcularPorcentajeAvance();
        procesoRepository.save(proceso);
        
        proceso.getMacroproceso().calcularPorcentajeAvance();
        macroprocesoRepository.save(proceso.getMacroproceso());
        
        return convertirADTO(updated);
    }
    
    /**
     * Convertir entidad a DTO
     */
    public SubprocesoResponseDTO convertirADTO(Subproceso subproceso) {
        SubprocesoResponseDTO dto = new SubprocesoResponseDTO();
        dto.setId(subproceso.getId());
        dto.setCodigo(subproceso.getCodigo());
        dto.setNombre(subproceso.getNombre());
        dto.setDescripcion(subproceso.getDescripcion());
        dto.setProcesoId(subproceso.getProceso().getId());
        dto.setProcesoNombre(subproceso.getProceso().getNombre());
        dto.setEstadoDocumentacion(subproceso.getEstadoDocumentacion());
        dto.setPorcentajeAvance(subproceso.getPorcentajeAvance());
        dto.setFechaCreacion(subproceso.getFechaCreacion());
        dto.setFechaActualizacion(subproceso.getFechaActualizacion());
        dto.setCreadoPor(subproceso.getCreadoPor());
        dto.setActualizadoPor(subproceso.getActualizadoPor());
        return dto;
    }
}
