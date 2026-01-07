package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.ProcesoRequestDTO;
import ec.edu.espe.inventario.model.dto.ProcesoResponseDTO;
import ec.edu.espe.inventario.model.entity.Macroproceso;
import ec.edu.espe.inventario.model.entity.Proceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.model.enums.TipoMacroproceso;
import ec.edu.espe.inventario.repository.MacroprocesoRepository;
import ec.edu.espe.inventario.repository.ProcesoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ProcesoService
 */
@ExtendWith(MockitoExtension.class)
class ProcesoServiceTest {

    @Mock
    private ProcesoRepository procesoRepository;

    @Mock
    private MacroprocesoRepository macroprocesoRepository;

    @Mock
    private SubprocesoService subprocesoService;

    @InjectMocks
    private ProcesoService procesoService;

    private ProcesoRequestDTO requestDTO;
    private Proceso proceso;
    private Macroproceso macroproceso;

    @BeforeEach
    void setUp() {
        // Preparar macroproceso padre
        macroproceso = new Macroproceso();
        macroproceso.setId(1L);
        macroproceso.setCodigo("VDC-MP-001");
        macroproceso.setTipo(TipoMacroproceso.VDC);
        macroproceso.setNombre("Gestión de Calidad");
        macroproceso.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);
        macroproceso.setProcesos(new ArrayList<>());

        // Preparar DTO de solicitud
        requestDTO = new ProcesoRequestDTO();
        requestDTO.setMacroprocesoId(1L);
        requestDTO.setNombre("Proceso de Auditoría");
        requestDTO.setDescripcion("Proceso de auditoría interna");
        requestDTO.setObjetivos("Realizar auditorías internas");
        requestDTO.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);

        // Preparar entidad proceso
        proceso = new Proceso();
        proceso.setId(1L);
        proceso.setCodigo("VDC-MP-001-P-001");
        proceso.setNombre("Proceso de Auditoría");
        proceso.setDescripcion("Proceso de auditoría interna");
        proceso.setObjetivos("Realizar auditorías internas");
        proceso.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);
        proceso.setMacroproceso(macroproceso);
        proceso.setSubprocesos(new ArrayList<>());
        proceso.setPorcentajeAvance(0);
        
        // Configurar macroproceso para evitar NullPointerException
        macroproceso.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);
    }

    @Test
    void testCrear_Exitoso() {
        // Arrange
        when(macroprocesoRepository.findById(1L)).thenReturn(Optional.of(macroproceso));
        when(procesoRepository.count()).thenReturn(0L);
        when(procesoRepository.existsByCodigo(anyString())).thenReturn(false);
        when(procesoRepository.save(any(Proceso.class))).thenReturn(proceso);
        when(macroprocesoRepository.save(any(Macroproceso.class))).thenReturn(macroproceso);

        // Act
        ProcesoResponseDTO resultado = procesoService.crear(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Proceso de Auditoría", resultado.getNombre());
        assertEquals(1L, resultado.getMacroprocesoId());
        verify(procesoRepository, times(1)).save(any(Proceso.class));
    }

    @Test
    void testCrear_MacroprocesoNoEncontrado() {
        // Arrange
        when(macroprocesoRepository.findById(999L)).thenReturn(Optional.empty());
        requestDTO.setMacroprocesoId(999L);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            procesoService.crear(requestDTO);
        });
        verify(procesoRepository, never()).save(any(Proceso.class));
    }

    @Test
    void testObtenerTodos_RetornaLista() {
        // Arrange
        Proceso proceso2 = new Proceso();
        proceso2.setId(2L);
        proceso2.setCodigo("VDC-MP-001-P-002");
        proceso2.setNombre("Proceso de Evaluación");
        proceso2.setMacroproceso(macroproceso);
        proceso2.setSubprocesos(new ArrayList<>());
        
        when(procesoRepository.findAll()).thenReturn(Arrays.asList(proceso, proceso2));

        // Act
        List<ProcesoResponseDTO> resultado = procesoService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void testObtenerPorId_Exitoso() {
        // Arrange
        when(procesoRepository.findByIdWithSubprocesos(1L)).thenReturn(Optional.of(proceso));

        // Act
        ProcesoResponseDTO resultado = procesoService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Proceso de Auditoría", resultado.getNombre());
    }

    @Test
    void testObtenerPorId_NoEncontrado() {
        // Arrange
        when(procesoRepository.findByIdWithSubprocesos(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            procesoService.obtenerPorId(999L);
        });
    }

    @Test
    void testObtenerPorMacroproceso_Exitoso() {
        // Arrange
        when(procesoRepository.findByMacroprocesoId(1L)).thenReturn(Arrays.asList(proceso));

        // Act
        List<ProcesoResponseDTO> resultado = procesoService.obtenerPorMacroproceso(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Proceso de Auditoría", resultado.get(0).getNombre());
    }

    @Test
    void testActualizar_Exitoso() {
        // Arrange
        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
        when(procesoRepository.save(any(Proceso.class))).thenReturn(proceso);

        requestDTO.setNombre("Proceso Actualizado");

        // Act
        ProcesoResponseDTO resultado = procesoService.actualizar(1L, requestDTO);

        // Assert
        assertNotNull(resultado);
        verify(procesoRepository, times(1)).save(any(Proceso.class));
        verify(macroprocesoRepository, times(1)).save(any(Macroproceso.class));
    }

    @Test
    void testEliminar_Exitoso() {
        // Arrange
        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
        doNothing().when(procesoRepository).delete(any(Proceso.class));

        // Act
        procesoService.eliminar(1L);

        // Assert
        verify(procesoRepository, times(1)).delete(proceso);
    }

    @Test
    void testEliminar_ConSubprocesosAsociados_LanzaExcepcion() {
        // Arrange
        proceso.getSubprocesos().add(new ec.edu.espe.inventario.model.entity.Subproceso());
        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            procesoService.eliminar(1L);
        });
        
        assertTrue(exception.getMessage().contains("tiene subprocesos asociados"));
        verify(procesoRepository, never()).delete(any(Proceso.class));
    }

    @Test
    void testActualizarEstado_Exitoso() {
        // Arrange
        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
        when(procesoRepository.save(any(Proceso.class))).thenReturn(proceso);

        // Act
        ProcesoResponseDTO resultado = procesoService.actualizarEstado(1L, EstadoDocumentacion.LEVANTAMIENTO);

        // Assert
        assertNotNull(resultado);
        verify(procesoRepository, times(1)).save(any(Proceso.class));
    }

    @Test
    void testGenerarCodigo_Unico() {
        // Arrange
        when(macroprocesoRepository.findById(1L)).thenReturn(Optional.of(macroproceso));
        when(procesoRepository.count()).thenReturn(5L);
        when(procesoRepository.existsByCodigo(anyString())).thenReturn(false);
        when(procesoRepository.save(any(Proceso.class))).thenReturn(proceso);
        when(macroprocesoRepository.save(any(Macroproceso.class))).thenReturn(macroproceso);

        // Act
        ProcesoResponseDTO resultado = procesoService.crear(requestDTO);

        // Assert
        assertNotNull(resultado);
        verify(procesoRepository, atLeast(1)).existsByCodigo(anyString());
    }
}
