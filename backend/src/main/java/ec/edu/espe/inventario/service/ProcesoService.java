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
 * Servicio para la gestión de Procesos (N1 y N2)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProcesoService {

    private final ProcesoRepository procesoRepository;
    private final MacroprocesoRepository macroprocesoRepository;
    private final SubprocesoService subprocesoService;

    // -------------------------------------------------------------------------
    // Generación de código
    // -------------------------------------------------------------------------

    /**
     * Código para Proceso N1: {COD_MACRO}-P-{NUM}
     */
    private String generarCodigoN1(String codigoMacro, Long macroprocesoId) {
        long count = procesoRepository.countByMacroprocesoIdAndNivel(macroprocesoId, 1) + 1;
        String codigo;
        do {
            codigo = String.format("%s-P-%02d", codigoMacro, count++);
        } while (procesoRepository.existsByCodigo(codigo));
        return codigo;
    }

    /**
     * Código para Proceso N2: {COD_N1}-N2-{NUM}
     */
    private String generarCodigoN2(String codigoPadre, Long padreId) {
        long count = procesoRepository.countByProcesoPadreId(padreId) + 1;
        String codigo;
        do {
            codigo = String.format("%s-N2-%02d", codigoPadre, count++);
        } while (procesoRepository.existsByCodigo(codigo));
        return codigo;
    }

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------

    /**
     * Crear un nuevo Proceso N1 o N2
     */
    @Transactional
    public ProcesoResponseDTO crear(ProcesoRequestDTO request) {
        int nivel = request.getNivel() != null ? request.getNivel() : 1;
        log.info("Creando Proceso N{}: {}", nivel, request.getNombre());

        Macroproceso macroproceso = macroprocesoRepository.findById(request.getMacroprocesoId())
                .orElseThrow(() -> new RuntimeException("Macroproceso no encontrado con ID: " + request.getMacroprocesoId()));

        Proceso proceso = new Proceso();
        proceso.setNivel(nivel);
        proceso.setNombre(request.getNombre());
        proceso.setDescripcion(request.getDescripcion());
        proceso.setObjetivos(request.getObjetivos());
        proceso.setMacroproceso(macroproceso);
        proceso.setEstadoDocumentacion(
                request.getEstadoDocumentacion() != null
                        ? request.getEstadoDocumentacion()
                        : EstadoDocumentacion.NO_DOCUMENTADO);
        proceso.setPorcentajeAvance(0);
        proceso.setCreadoPor("admin");
        proceso.setActualizadoPor("admin");

        if (nivel == 1) {
            proceso.setCodigo(generarCodigoN1(macroproceso.getCodigo(), macroproceso.getId()));
        } else {
            if (request.getProcesoPadreId() == null) {
                throw new RuntimeException("procesoPadreId es obligatorio para Proceso N2");
            }
            Proceso padre = procesoRepository.findById(request.getProcesoPadreId())
                    .orElseThrow(() -> new RuntimeException("Proceso N1 padre no encontrado con ID: " + request.getProcesoPadreId()));
            if (padre.getNivel() != 1) {
                throw new RuntimeException("El proceso padre debe ser de nivel 1");
            }
            proceso.setProcesoPadre(padre);
            proceso.setCodigo(generarCodigoN2(padre.getCodigo(), padre.getId()));
        }

        Proceso saved = procesoRepository.save(proceso);
        propagarAvanceHaciaArriba(saved);

        log.info("Proceso N{} creado con ID: {}", nivel, saved.getId());
        return convertirADTOSimple(saved);
    }

    /**
     * Obtener todos los procesos
     */
    @Transactional(readOnly = true)
    public List<ProcesoResponseDTO> obtenerTodos() {
        return procesoRepository.findAll().stream()
                .map(this::convertirADTOSimple)
                .collect(Collectors.toList());
    }

    /**
     * Obtener procesos por macroproceso (todos los niveles)
     */
    @Transactional(readOnly = true)
    public List<ProcesoResponseDTO> obtenerPorMacroproceso(Long macroprocesoId) {
        return procesoRepository.findByMacroprocesoId(macroprocesoId).stream()
                .map(this::convertirADTOSimple)
                .collect(Collectors.toList());
    }

    /**
     * Obtener procesos por macroproceso filtrados por nivel
     */
    @Transactional(readOnly = true)
    public List<ProcesoResponseDTO> obtenerPorMacroprocesoYNivel(Long macroprocesoId, int nivel) {
        return procesoRepository.findByMacroprocesoIdAndNivel(macroprocesoId, nivel).stream()
                .map(this::convertirADTOSimple)
                .collect(Collectors.toList());
    }

    /**
     * Obtener Procesos N2 hijos de un Proceso N1
     */
    @Transactional(readOnly = true)
    public List<ProcesoResponseDTO> obtenerPorProcesoPadre(Long procesoPadreId) {
        return procesoRepository.findByProcesoPadreId(procesoPadreId).stream()
                .map(this::convertirADTOSimple)
                .collect(Collectors.toList());
    }

    /**
     * Obtener proceso por ID con sus subprocesos
     */
    @Transactional(readOnly = true)
    public ProcesoResponseDTO obtenerPorId(Long id) {
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
        propagarAvanceHaciaArriba(updated);

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

        if (proceso.getNivel() == 1 && !proceso.getProcesosHijos().isEmpty()) {
            throw new RuntimeException("No se puede eliminar: el Proceso N1 tiene Procesos N2 asociados");
        }
        if (proceso.getNivel() == 2 && !proceso.getSubprocesos().isEmpty()) {
            throw new RuntimeException("No se puede eliminar: el Proceso N2 tiene Subprocesos asociados");
        }

        Macroproceso macroproceso = proceso.getMacroproceso();
        Proceso padre = proceso.getProcesoPadre();
        procesoRepository.delete(proceso);

        if (padre != null) {
            padre = procesoRepository.findById(padre.getId()).orElse(null);
            if (padre != null) {
                padre.calcularPorcentajeAvance();
                procesoRepository.save(padre);
            }
        }
        macroproceso.calcularPorcentajeAvance();
        macroprocesoRepository.save(macroproceso);
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
        propagarAvanceHaciaArriba(updated);

        return convertirADTOSimple(updated);
    }

    // -------------------------------------------------------------------------
    // Propagación de avance hacia arriba
    // -------------------------------------------------------------------------

    /**
     * Propaga el recálculo de porcentaje de avance hacia los ancestros del proceso dado.
     * - Si es N2: recalcula su N1 padre, luego el Macroproceso.
     * - Si es N1: recalcula el Macroproceso directamente.
     */
    public void propagarAvanceHaciaArriba(Proceso proceso) {
        if (proceso.getNivel() == 2 && proceso.getProcesoPadre() != null) {
            Proceso n1 = procesoRepository.findById(proceso.getProcesoPadre().getId())
                    .orElse(null);
            if (n1 != null) {
                n1.calcularPorcentajeAvance();
                procesoRepository.save(n1);
                n1.getMacroproceso().calcularPorcentajeAvance();
                macroprocesoRepository.save(n1.getMacroproceso());
            }
        } else {
            proceso.getMacroproceso().calcularPorcentajeAvance();
            macroprocesoRepository.save(proceso.getMacroproceso());
        }
    }

    // -------------------------------------------------------------------------
    // Conversión a DTO
    // -------------------------------------------------------------------------

    public ProcesoResponseDTO convertirADTOSimple(Proceso proceso) {
        ProcesoResponseDTO dto = new ProcesoResponseDTO();
        dto.setId(proceso.getId());
        dto.setCodigo(proceso.getCodigo());
        dto.setNombre(proceso.getNombre());
        dto.setDescripcion(proceso.getDescripcion());
        dto.setObjetivos(proceso.getObjetivos());
        dto.setNivel(proceso.getNivel());
        dto.setMacroprocesoId(proceso.getMacroproceso().getId());
        dto.setMacroprocesoNombre(proceso.getMacroproceso().getNombre());

        if (proceso.getProcesoPadre() != null) {
            dto.setProcesoPadreId(proceso.getProcesoPadre().getId());
            dto.setProcesoPadreNombre(proceso.getProcesoPadre().getNombre());
        }

        dto.setEstadoDocumentacion(proceso.getEstadoDocumentacion());
        dto.setPorcentajeAvance(proceso.getPorcentajeAvance());
        dto.setFechaCreacion(proceso.getFechaCreacion());
        dto.setFechaActualizacion(proceso.getFechaActualizacion());
        dto.setCreadoPor(proceso.getCreadoPor());
        dto.setActualizadoPor(proceso.getActualizadoPor());

        dto.setCantidadProcesosHijos(
                proceso.getProcesosHijos() != null ? proceso.getProcesosHijos().size() : 0);
        dto.setCantidadSubprocesos(
                proceso.getSubprocesos() != null ? proceso.getSubprocesos().size() : 0);

        return dto;
    }

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
