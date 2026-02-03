package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.InformacionDocumentadaRequestDTO;
import ec.edu.espe.inventario.model.dto.InformacionDocumentadaResponseDTO;
import ec.edu.espe.inventario.model.entity.InformacionDocumentada;
import ec.edu.espe.inventario.model.entity.Macroproceso;
import ec.edu.espe.inventario.model.entity.Proceso;
import ec.edu.espe.inventario.model.entity.Subproceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumento;
import ec.edu.espe.inventario.repository.InformacionDocumentadaRepository;
import ec.edu.espe.inventario.repository.MacroprocesoRepository;
import ec.edu.espe.inventario.repository.ProcesoRepository;
import ec.edu.espe.inventario.repository.SubprocesoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InformacionDocumentadaService {

    private final InformacionDocumentadaRepository informacionDocumentadaRepository;
    private final MacroprocesoRepository macroprocesoRepository;
    private final ProcesoRepository procesoRepository;
    private final SubprocesoRepository subprocesoRepository;

    @Transactional
    public InformacionDocumentadaResponseDTO crear(InformacionDocumentadaRequestDTO requestDTO) {
        InformacionDocumentada informacionDocumentada = new InformacionDocumentada();
        mapearDTOAEntidad(requestDTO, informacionDocumentada);
        
        // Generar código de documento automáticamente si no viene en el request
        if (requestDTO.getCodigoDocumento() == null || requestDTO.getCodigoDocumento().isEmpty()) {
            String codigoGenerado = generarCodigoDocumento(requestDTO);
            informacionDocumentada.setCodigoDocumento(codigoGenerado);
        }
        
        // Calcular mes y año si no vienen
        if (requestDTO.getMes() == null) {
            informacionDocumentada.setMes(LocalDate.now().getMonthValue());
        }
        if (requestDTO.getAnio() == null) {
            informacionDocumentada.setAnio(LocalDate.now().getYear());
        }
        
        InformacionDocumentada guardado = informacionDocumentadaRepository.save(informacionDocumentada);
        return mapearEntidadADTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<InformacionDocumentadaResponseDTO> listarTodos() {
        return informacionDocumentadaRepository.findByEstadoOrderByFechaSolicitudDesc(EstadoDocumento.ACTIVO)
                .stream()
                .map(this::mapearEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InformacionDocumentadaResponseDTO> listarPorSede(String sede) {
        return informacionDocumentadaRepository.findBySedeAndEstadoOrderByFechaSolicitudDesc(sede, EstadoDocumento.ACTIVO)
                .stream()
                .map(this::mapearEntidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InformacionDocumentadaResponseDTO obtenerPorId(Long id) {
        InformacionDocumentada informacionDocumentada = informacionDocumentadaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Información documentada no encontrada con id: " + id));
        return mapearEntidadADTO(informacionDocumentada);
    }

    @Transactional
    public InformacionDocumentadaResponseDTO actualizar(Long id, InformacionDocumentadaRequestDTO requestDTO) {
        InformacionDocumentada informacionDocumentada = informacionDocumentadaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Información documentada no encontrada con id: " + id));
        
        mapearDTOAEntidad(requestDTO, informacionDocumentada);
        
        InformacionDocumentada actualizado = informacionDocumentadaRepository.save(informacionDocumentada);
        return mapearEntidadADTO(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        InformacionDocumentada informacionDocumentada = informacionDocumentadaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Información documentada no encontrada con id: " + id));
        
        // Eliminación lógica
        informacionDocumentada.setEstado(EstadoDocumento.OBSOLETO);
        informacionDocumentada.setFechaEliminacion(LocalDate.now());
        informacionDocumentadaRepository.save(informacionDocumentada);
    }

    private void mapearDTOAEntidad(InformacionDocumentadaRequestDTO dto, InformacionDocumentada entidad) {
        if (dto.getFechaSolicitud() != null) {
            entidad.setFechaSolicitud(dto.getFechaSolicitud());
        }
        entidad.setUnidad(dto.getUnidad());
        entidad.setSolicitadoPor(dto.getSolicitadoPor());
        entidad.setSede(dto.getSede());
        
        // Mapear relaciones
        if (dto.getMacroprocesoId() != null) {
            Macroproceso macroproceso = macroprocesoRepository.findById(dto.getMacroprocesoId())
                    .orElseThrow(() -> new RuntimeException("Macroproceso no encontrado"));
            entidad.setMacroproceso(macroproceso);
        }
        
        if (dto.getProcesoN1Id() != null) {
            Proceso proceso = procesoRepository.findById(dto.getProcesoN1Id())
                    .orElseThrow(() -> new RuntimeException("Proceso N1 no encontrado"));
            entidad.setProcesoN1(proceso);
        }
        
        if (dto.getProcesoN2Id() != null) {
            Proceso proceso = procesoRepository.findById(dto.getProcesoN2Id())
                    .orElseThrow(() -> new RuntimeException("Proceso N2 no encontrado"));
            entidad.setProcesoN2(proceso);
        }
        
        if (dto.getSubprocesoN1Id() != null) {
            Subproceso subproceso = subprocesoRepository.findById(dto.getSubprocesoN1Id())
                    .orElseThrow(() -> new RuntimeException("Subproceso N1 no encontrado"));
            entidad.setSubprocesoN1(subproceso);
        }
        
        if (dto.getSubprocesoN2Id() != null) {
            Subproceso subproceso = subprocesoRepository.findById(dto.getSubprocesoN2Id())
                    .orElseThrow(() -> new RuntimeException("Subproceso N2 no encontrado"));
            entidad.setSubprocesoN2(subproceso);
        }
        
        entidad.setTipoDocumento(dto.getTipoDocumento());
        entidad.setNombreDocumento(dto.getNombreDocumento());
        entidad.setFechaProtocolo(dto.getFechaProtocolo());
        entidad.setLugarEvento(dto.getLugarEvento());
        entidad.setEnlaceArchivo(dto.getEnlaceArchivo());
        entidad.setMotivo(dto.getMotivo());
        entidad.setObservaciones(dto.getObservaciones());
        entidad.setCodigoProceso(dto.getCodigoProceso());
        
        if (dto.getEstado() != null) {
            entidad.setEstado(EstadoDocumento.valueOf(dto.getEstado()));
        }
        
        entidad.setObservacionesUpdi(dto.getObservacionesUpdi());
        entidad.setCodificadoPor(dto.getCodificadoPor());
        entidad.setMes(dto.getMes());
        entidad.setAnio(dto.getAnio());
        entidad.setVersion(dto.getVersion());
        entidad.setSecuencial(dto.getSecuencial());
    }

    private InformacionDocumentadaResponseDTO mapearEntidadADTO(InformacionDocumentada entidad) {
        InformacionDocumentadaResponseDTO dto = new InformacionDocumentadaResponseDTO();
        dto.setId(entidad.getId());
        dto.setFechaSolicitud(entidad.getFechaSolicitud());
        dto.setUnidad(entidad.getUnidad());
        dto.setSolicitadoPor(entidad.getSolicitadoPor());
        dto.setSede(entidad.getSede());
        
        if (entidad.getMacroproceso() != null) {
            dto.setMacroprocesoId(entidad.getMacroproceso().getId());
            dto.setMacroprocesoNombre(entidad.getMacroproceso().getNombre());
        }
        
        if (entidad.getProcesoN1() != null) {
            dto.setProcesoN1Id(entidad.getProcesoN1().getId());
            dto.setProcesoN1Nombre(entidad.getProcesoN1().getNombre());
        }
        
        if (entidad.getProcesoN2() != null) {
            dto.setProcesoN2Id(entidad.getProcesoN2().getId());
            dto.setProcesoN2Nombre(entidad.getProcesoN2().getNombre());
        }
        
        if (entidad.getSubprocesoN1() != null) {
            dto.setSubprocesoN1Id(entidad.getSubprocesoN1().getId());
            dto.setSubprocesoN1Nombre(entidad.getSubprocesoN1().getNombre());
        }
        
        if (entidad.getSubprocesoN2() != null) {
            dto.setSubprocesoN2Id(entidad.getSubprocesoN2().getId());
            dto.setSubprocesoN2Nombre(entidad.getSubprocesoN2().getNombre());
        }
        
        dto.setTipoDocumento(entidad.getTipoDocumento());
        dto.setNombreDocumento(entidad.getNombreDocumento());
        dto.setFechaProtocolo(entidad.getFechaProtocolo());
        dto.setLugarEvento(entidad.getLugarEvento());
        dto.setEnlaceArchivo(entidad.getEnlaceArchivo());
        dto.setMotivo(entidad.getMotivo());
        dto.setObservaciones(entidad.getObservaciones());
        dto.setCodigoDocumento(entidad.getCodigoDocumento());
        dto.setCodigoProceso(entidad.getCodigoProceso());
        dto.setEstado(entidad.getEstado());
        dto.setFechaEliminacion(entidad.getFechaEliminacion());
        dto.setObservacionesUpdi(entidad.getObservacionesUpdi());
        dto.setCodificadoPor(entidad.getCodificadoPor());
        dto.setMes(entidad.getMes());
        dto.setAnio(entidad.getAnio());
        dto.setVersion(entidad.getVersion());
        dto.setSecuencial(entidad.getSecuencial());
        dto.setCreatedAt(entidad.getCreatedAt());
        dto.setUpdatedAt(entidad.getUpdatedAt());
        
        return dto;
    }

    /**
     * Genera el código de documento siguiendo el formato:
     * UNIDAD-TIPODOCUMENTO(SIGLAS)-ANIO-VERSION-SECUENCIA
     * Ejemplo: UAFA-FOR-2024-V1-029
     */
    private String generarCodigoDocumento(InformacionDocumentadaRequestDTO dto) {
        int anio = dto.getAnio() != null ? dto.getAnio() : LocalDate.now().getYear();
        String version = dto.getVersion() != null ? dto.getVersion() : "V1";
        
        // Obtener siglas de la unidad (primeras 4 letras o las disponibles)
        String siglasUnidad = obtenerSiglasUnidad(dto.getUnidad());
        
        // Obtener siglas del tipo de documento (primeras 3 letras)
        String siglasTipoDoc = obtenerSiglasTipoDocumento(dto.getTipoDocumento());
        
        // Calcular secuencial
        Integer maxSecuencial = informacionDocumentadaRepository
                .findMaxSecuencialByAnioAndTipoDocumentoAndUnidad(anio, dto.getTipoDocumento(), dto.getUnidad());
        int secuencial = (maxSecuencial != null ? maxSecuencial : 0) + 1;
        
        // Formatear secuencial con 3 dígitos
        String secuencialFormateado = String.format("%03d", secuencial);
        
        return String.format("%s-%s-%d-%s-%s", 
                siglasUnidad, 
                siglasTipoDoc, 
                anio, 
                version, 
                secuencialFormateado);
    }

    private String obtenerSiglasUnidad(String unidad) {
        if (unidad == null || unidad.isEmpty()) {
            return "UNID";
        }
        
        // Remover espacios y caracteres especiales
        String unidadLimpia = unidad.replaceAll("[^a-zA-Z]", "").toUpperCase();
        
        // Si tiene menos de 4 caracteres, retornar lo que haya
        if (unidadLimpia.length() <= 4) {
            return unidadLimpia;
        }
        
        // Retornar las primeras 4 letras
        return unidadLimpia.substring(0, 4);
    }

    private String obtenerSiglasTipoDocumento(String tipoDocumento) {
        if (tipoDocumento == null || tipoDocumento.isEmpty()) {
            return "DOC";
        }
        
        // Remover espacios y caracteres especiales
        String tipoLimpio = tipoDocumento.replaceAll("[^a-zA-Z]", "").toUpperCase();
        
        // Si tiene menos de 3 caracteres, retornar lo que haya
        if (tipoLimpio.length() <= 3) {
            return tipoLimpio;
        }
        
        // Retornar las primeras 3 letras
        return tipoLimpio.substring(0, 3);
    }
}
