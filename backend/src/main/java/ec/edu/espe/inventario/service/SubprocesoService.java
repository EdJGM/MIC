package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.SubprocesoRequestDTO;
import ec.edu.espe.inventario.model.dto.SubprocesoResponseDTO;
import ec.edu.espe.inventario.model.entity.Proceso;
import ec.edu.espe.inventario.model.entity.Subproceso;
import ec.edu.espe.inventario.model.enums.AccionAudit;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.model.enums.TipoNotificacion;
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
 * Servicio para la gestión de Subprocesos (SP-N1 y SP-N2)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubprocesoService {

    private static final String USUARIO_ID = "admin-espe-001";
    private static final String USUARIO_NOMBRE = "Administrador";
    private static final String MODULO = "SUBPROCESOS";

    private final SubprocesoRepository subprocesoRepository;
    private final ProcesoRepository procesoRepository;
    private final MacroprocesoRepository macroprocesoRepository;
    private final AuditLogService auditLogService;
    private final NotificacionService notificacionService;

    // -------------------------------------------------------------------------
    // Generación de código
    // -------------------------------------------------------------------------

    /**
     * Código SP-N1: {COD_PROCESO_N2}-SP-{NUM}
     */
    private String generarCodigoSpN1(String codigoProcesoN2, Long procesoId) {
        long count = subprocesoRepository.countByProcesoId(procesoId) + 1;
        String codigo;
        do {
            codigo = String.format("%s-SP-%02d", codigoProcesoN2, count++);
        } while (subprocesoRepository.existsByCodigo(codigo));
        return codigo;
    }

    /**
     * Código SP-N2: {COD_SP_N1}-SPN2-{NUM}
     */
    private String generarCodigoSpN2(String codigoSpN1, Long spN1Id) {
        long count = subprocesoRepository.countBySubprocesoPadreId(spN1Id) + 1;
        String codigo;
        do {
            codigo = String.format("%s-SPN2-%02d", codigoSpN1, count++);
        } while (subprocesoRepository.existsByCodigo(codigo));
        return codigo;
    }

    // -------------------------------------------------------------------------
    // CRUD
    // -------------------------------------------------------------------------

    /**
     * Crear un nuevo Subproceso (SP-N1 o SP-N2)
     */
    @Transactional
    public SubprocesoResponseDTO crear(SubprocesoRequestDTO request) {
        int nivel = request.getNivel() != null ? request.getNivel() : 1;
        log.info("Creando Subproceso SP-N{}: {}", nivel, request.getNombre());

        Subproceso subproceso = new Subproceso();
        subproceso.setNivel(nivel);
        subproceso.setNombre(request.getNombre());
        subproceso.setDescripcion(request.getDescripcion());
        subproceso.setEstadoDocumentacion(
                request.getEstadoDocumentacion() != null
                        ? request.getEstadoDocumentacion()
                        : EstadoDocumentacion.NO_DOCUMENTADO);
        subproceso.setCreadoPor("admin");
        subproceso.setActualizadoPor("admin");

        if (nivel == 1) {
            if (request.getProcesoId() == null) {
                throw new RuntimeException("procesoId es obligatorio para Subproceso N1");
            }
            Proceso proceso = procesoRepository.findById(request.getProcesoId())
                    .orElseThrow(() -> new RuntimeException("Proceso no encontrado con ID: " + request.getProcesoId()));
            if (proceso.getNivel() != 2) {
                throw new RuntimeException("El proceso padre de un Subproceso N1 debe ser un Proceso N2");
            }
            subproceso.setProceso(proceso);
            subproceso.setCodigo(generarCodigoSpN1(proceso.getCodigo(), proceso.getId()));

        } else {
            // nivel == 2
            if (request.getSubprocesoPadreId() == null) {
                throw new RuntimeException("subprocesoPadreId es obligatorio para Subproceso N2");
            }
            Subproceso padre = subprocesoRepository.findById(request.getSubprocesoPadreId())
                    .orElseThrow(() -> new RuntimeException("Subproceso N1 padre no encontrado con ID: " + request.getSubprocesoPadreId()));
            if (padre.getNivel() != 1) {
                throw new RuntimeException("El subproceso padre debe ser de nivel 1");
            }
            subproceso.setSubprocesoPadre(padre);

            // Heredar el proceso del padre o usar el proporcionado (deben coincidir)
            Proceso proceso;
            if (request.getProcesoId() != null) {
                proceso = procesoRepository.findById(request.getProcesoId())
                        .orElseThrow(() -> new RuntimeException("Proceso no encontrado con ID: " + request.getProcesoId()));
            } else {
                proceso = padre.getProceso();
            }
            subproceso.setProceso(proceso);
            subproceso.setCodigo(generarCodigoSpN2(padre.getCodigo(), padre.getId()));
        }

        Subproceso saved = subprocesoRepository.save(subproceso);
        propagarAvanceHaciaArriba(saved);

        log.info("Subproceso SP-N{} creado con ID: {}", nivel, saved.getId());
        auditLogService.registrar(USUARIO_ID, USUARIO_NOMBRE, AccionAudit.CREAR,
                MODULO, saved.getId(), "Subproceso SP-N" + nivel + " creado: " + saved.getNombre());

        return convertirADTO(saved);
    }

    /**
     * Obtener todos los subprocesos
     */
    @Transactional(readOnly = true)
    public List<SubprocesoResponseDTO> obtenerTodos() {
        return subprocesoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener subprocesos por proceso (todos los niveles)
     */
    @Transactional(readOnly = true)
    public List<SubprocesoResponseDTO> obtenerPorProceso(Long procesoId) {
        return subprocesoRepository.findByProcesoId(procesoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener subprocesos por proceso filtrados por nivel
     */
    @Transactional(readOnly = true)
    public List<SubprocesoResponseDTO> obtenerPorProcesoYNivel(Long procesoId, int nivel) {
        return subprocesoRepository.findByProcesoIdAndNivel(procesoId, nivel).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener Subprocesos N2 hijos de un Subproceso N1
     */
    @Transactional(readOnly = true)
    public List<SubprocesoResponseDTO> obtenerPorSubprocesoPadre(Long subprocesoPadreId) {
        return subprocesoRepository.findBySubprocesoPadreId(subprocesoPadreId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener subproceso por ID
     */
    @Transactional(readOnly = true)
    public SubprocesoResponseDTO obtenerPorId(Long id) {
        Subproceso subproceso = subprocesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subproceso no encontrado con ID: " + id));
        return convertirADTO(subproceso);
    }

    /**
     * Actualizar subproceso (nombre, descripción, estado y jerarquía)
     */
    @Transactional
    public SubprocesoResponseDTO actualizar(Long id, SubprocesoRequestDTO request) {
        log.info("Actualizando subproceso con ID: {}", id);

        Subproceso subproceso = subprocesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subproceso no encontrado con ID: " + id));

        // Actualizar campos de datos
        subproceso.setNombre(request.getNombre());
        subproceso.setDescripcion(request.getDescripcion());
        if (request.getEstadoDocumentacion() != null) {
            subproceso.setEstadoDocumentacion(request.getEstadoDocumentacion());
        }

        // Actualizar proceso padre (Proceso N2) si cambió
        if (request.getProcesoId() != null
                && !request.getProcesoId().equals(subproceso.getProceso().getId())) {
            Proceso proceso = procesoRepository.findById(request.getProcesoId())
                    .orElseThrow(() -> new RuntimeException("Proceso no encontrado con ID: " + request.getProcesoId()));
            if (proceso.getNivel() != 2) {
                throw new RuntimeException("El proceso de un Subproceso N1 debe ser un Proceso N2");
            }
            subproceso.setProceso(proceso);
        }

        // Actualizar nivel y subproceso padre
        int nivel = request.getNivel() != null ? request.getNivel() : subproceso.getNivel();
        subproceso.setNivel(nivel);
        if (nivel == 2) {
            if (request.getSubprocesoPadreId() == null) {
                throw new RuntimeException("subprocesoPadreId es obligatorio para Subproceso N2");
            }
            Subproceso padre = subprocesoRepository.findById(request.getSubprocesoPadreId())
                    .orElseThrow(() -> new RuntimeException("Subproceso N1 padre no encontrado con ID: " + request.getSubprocesoPadreId()));
            if (padre.getNivel() != 1) {
                throw new RuntimeException("El subproceso padre debe ser de nivel 1");
            }
            subproceso.setSubprocesoPadre(padre);
        } else {
            subproceso.setSubprocesoPadre(null);
        }

        subproceso.setActualizadoPor("admin");
        subproceso.calcularPorcentajeAvance();

        Subproceso updated = subprocesoRepository.save(subproceso);
        propagarAvanceHaciaArriba(updated);

        auditLogService.registrar(USUARIO_ID, USUARIO_NOMBRE, AccionAudit.ACTUALIZAR,
                MODULO, updated.getId(), "Subproceso actualizado: " + updated.getNombre());

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

        if (subproceso.getNivel() == 1 && !subproceso.getSubprocesosHijos().isEmpty()) {
            throw new RuntimeException("No se puede eliminar: el Subproceso N1 tiene Subprocesos N2 asociados");
        }

        Proceso proceso = subproceso.getProceso();
        Subproceso padre = subproceso.getSubprocesoPadre();
        String nombre = subproceso.getNombre();
        int nivel = subproceso.getNivel();
        subprocesoRepository.delete(subproceso);

        if (padre != null) {
            padre = subprocesoRepository.findById(padre.getId()).orElse(null);
            if (padre != null) {
                padre.calcularPorcentajeAvance();
                subprocesoRepository.save(padre);
            }
        }

        recalcularCadenaProceso(proceso);

        auditLogService.registrar(USUARIO_ID, USUARIO_NOMBRE, AccionAudit.ELIMINAR,
                MODULO, id, "Subproceso SP-N" + nivel + " eliminado: " + nombre);
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
        subproceso.calcularPorcentajeAvance();
        subproceso.setActualizadoPor("admin");

        Subproceso updated = subprocesoRepository.save(subproceso);
        propagarAvanceHaciaArriba(updated);

        auditLogService.registrar(USUARIO_ID, USUARIO_NOMBRE, AccionAudit.CAMBIO_ESTADO,
                MODULO, updated.getId(), "Estado actualizado a " + nuevoEstado + " en: " + updated.getNombre());
        notificacionService.crear(TipoNotificacion.SISTEMA,
                "Estado de subproceso actualizado",
                "El subproceso \"" + updated.getNombre() + "\" cambió a estado: " + nuevoEstado.name(),
                USUARIO_ID, updated.getId(), MODULO);

        return convertirADTO(updated);
    }

    // -------------------------------------------------------------------------
    // Propagación de avance hacia arriba
    // -------------------------------------------------------------------------

    /**
     * Propaga el recálculo desde el subproceso dado hasta el Macroproceso.
     *
     * Cadena completa:
     *   SP-N2 → SP-N1 → Proceso N2 → Proceso N1 → Macroproceso
     *   SP-N1          → Proceso N2 → Proceso N1 → Macroproceso
     */
    private void propagarAvanceHaciaArriba(Subproceso subproceso) {
        // Si es SP-N2, primero actualizar su SP-N1 padre
        if (subproceso.getNivel() == 2 && subproceso.getSubprocesoPadre() != null) {
            Subproceso spN1 = subprocesoRepository.findById(subproceso.getSubprocesoPadre().getId())
                    .orElse(null);
            if (spN1 != null) {
                spN1.calcularPorcentajeAvance();
                subprocesoRepository.save(spN1);
            }
        }
        // Luego subir por la cadena de procesos
        recalcularCadenaProceso(subproceso.getProceso());
    }

    /**
     * Recalcula el avance de Proceso N2 → Proceso N1 → Macroproceso.
     */
    private void recalcularCadenaProceso(Proceso procesoN2) {
        if (procesoN2 == null) return;

        Proceso n2 = procesoRepository.findById(procesoN2.getId()).orElse(null);
        if (n2 == null) return;

        n2.calcularPorcentajeAvance();
        procesoRepository.save(n2);

        if (n2.getProcesoPadre() != null) {
            Proceso n1 = procesoRepository.findById(n2.getProcesoPadre().getId()).orElse(null);
            if (n1 != null) {
                n1.calcularPorcentajeAvance();
                procesoRepository.save(n1);
                n1.getMacroproceso().calcularPorcentajeAvance();
                macroprocesoRepository.save(n1.getMacroproceso());
            }
        } else {
            n2.getMacroproceso().calcularPorcentajeAvance();
            macroprocesoRepository.save(n2.getMacroproceso());
        }
    }

    // -------------------------------------------------------------------------
    // Conversión a DTO
    // -------------------------------------------------------------------------

    public SubprocesoResponseDTO convertirADTO(Subproceso subproceso) {
        SubprocesoResponseDTO dto = new SubprocesoResponseDTO();
        dto.setId(subproceso.getId());
        dto.setCodigo(subproceso.getCodigo());
        dto.setNombre(subproceso.getNombre());
        dto.setDescripcion(subproceso.getDescripcion());
        dto.setNivel(subproceso.getNivel());
        dto.setProcesoId(subproceso.getProceso().getId());
        dto.setProcesoNombre(subproceso.getProceso().getNombre());

        if (subproceso.getSubprocesoPadre() != null) {
            dto.setSubprocesoPadreId(subproceso.getSubprocesoPadre().getId());
            dto.setSubprocesoPadreNombre(subproceso.getSubprocesoPadre().getNombre());
        }

        dto.setEstadoDocumentacion(subproceso.getEstadoDocumentacion());
        dto.setPorcentajeAvance(subproceso.getPorcentajeAvance());
        dto.setFechaCreacion(subproceso.getFechaCreacion());
        dto.setFechaActualizacion(subproceso.getFechaActualizacion());
        dto.setCreadoPor(subproceso.getCreadoPor());
        dto.setActualizadoPor(subproceso.getActualizadoPor());

        dto.setCantidadSubprocesosHijos(
                subproceso.getSubprocesosHijos() != null ? subproceso.getSubprocesosHijos().size() : 0);

        return dto;
    }
}
