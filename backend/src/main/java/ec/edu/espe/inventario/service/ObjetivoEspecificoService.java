package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.ObjetivoEspecificoRequestDTO;
import ec.edu.espe.inventario.model.dto.ObjetivoEspecificoResponseDTO;
import ec.edu.espe.inventario.model.entity.ObjetivoEspecifico;
import ec.edu.espe.inventario.repository.ObjetivoEspecificoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestion de Objetivos Especificos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ObjetivoEspecificoService {

    private final ObjetivoEspecificoRepository objetivoEspecificoRepository;

    /**
     * Crear un nuevo objetivo especifico
     */
    @Transactional
    public ObjetivoEspecificoResponseDTO crear(ObjetivoEspecificoRequestDTO request) {
        log.info("Creando nuevo objetivo especifico: {}", request.getNombre());

        if (objetivoEspecificoRepository.existsByNombre(request.getNombre())) {
            throw new RuntimeException("Ya existe un objetivo especifico con ese nombre");
        }

        ObjetivoEspecifico objetivo = new ObjetivoEspecifico();
        objetivo.setNombre(request.getNombre());
        objetivo.setDescripcion(request.getDescripcion());
        objetivo.setCreadoPor("admin");
        objetivo.setActualizadoPor("admin");

        ObjetivoEspecifico saved = objetivoEspecificoRepository.save(objetivo);
        log.info("Objetivo especifico creado con ID: {}", saved.getId());

        return convertirADTO(saved);
    }

    /**
     * Obtener todos los objetivos especificos
     */
    @Transactional(readOnly = true)
    public List<ObjetivoEspecificoResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los objetivos especificos");
        return objetivoEspecificoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener objetivo especifico por ID
     */
    @Transactional(readOnly = true)
    public ObjetivoEspecificoResponseDTO obtenerPorId(Long id) {
        log.info("Obteniendo objetivo especifico con ID: {}", id);
        ObjetivoEspecifico objetivo = objetivoEspecificoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Objetivo especifico no encontrado con ID: " + id));
        return convertirADTO(objetivo);
    }

    /**
     * Actualizar objetivo especifico
     */
    @Transactional
    public ObjetivoEspecificoResponseDTO actualizar(Long id, ObjetivoEspecificoRequestDTO request) {
        log.info("Actualizando objetivo especifico con ID: {}", id);

        ObjetivoEspecifico objetivo = objetivoEspecificoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Objetivo especifico no encontrado con ID: " + id));

        objetivo.setNombre(request.getNombre());
        objetivo.setDescripcion(request.getDescripcion());
        objetivo.setActualizadoPor("admin");

        ObjetivoEspecifico updated = objetivoEspecificoRepository.save(objetivo);
        log.info("Objetivo especifico actualizado: {}", updated.getId());

        return convertirADTO(updated);
    }

    /**
     * Eliminar objetivo especifico
     */
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando objetivo especifico con ID: {}", id);

        ObjetivoEspecifico objetivo = objetivoEspecificoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Objetivo especifico no encontrado con ID: " + id));

        objetivoEspecificoRepository.delete(objetivo);
        log.info("Objetivo especifico eliminado: {}", id);
    }

    /**
     * Convertir entidad a DTO
     */
    private ObjetivoEspecificoResponseDTO convertirADTO(ObjetivoEspecifico objetivo) {
        ObjetivoEspecificoResponseDTO dto = new ObjetivoEspecificoResponseDTO();
        dto.setId(objetivo.getId());
        dto.setNombre(objetivo.getNombre());
        dto.setDescripcion(objetivo.getDescripcion());
        dto.setFechaCreacion(objetivo.getFechaCreacion());
        dto.setFechaActualizacion(objetivo.getFechaActualizacion());
        dto.setCreadoPor(objetivo.getCreadoPor());
        dto.setActualizadoPor(objetivo.getActualizadoPor());
        return dto;
    }
}
