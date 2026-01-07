package ec.edu.espe.inventario.service;

import ec.edu.espe.inventario.model.dto.SubprocesoRequestDTO;
import ec.edu.espe.inventario.model.dto.SubprocesoResponseDTO;
import ec.edu.espe.inventario.model.entity.Macroproceso;
import ec.edu.espe.inventario.model.entity.Proceso;
import ec.edu.espe.inventario.model.entity.Subproceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.repository.MacroprocesoRepository;
import ec.edu.espe.inventario.repository.ProcesoRepository;
import ec.edu.espe.inventario.repository.SubprocesoRepository;
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
 * Pruebas unitarias para SubprocesoService
 */
@ExtendWith(MockitoExtension.class)
class SubprocesoServiceTest {

    @Mock
    private SubprocesoRepository subprocesoRepository;

    @Mock
    private ProcesoRepository procesoRepository;

    @Mock
    private MacroprocesoRepository macroprocesoRepository;

    @InjectMocks
    private SubprocesoService subprocesoService;

    private SubprocesoRequestDTO requestDTO;
    private Subproceso subproceso;
    private Proceso proceso;
    private Macroproceso macroproceso;

    @BeforeEach
    void setUp() {
        // Preparar macroproceso
        macroproceso = new Macroproceso();
        macroproceso.setId(1L);
        macroproceso.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);
        
        // Preparar proceso padre
        proceso = new Proceso();
        proceso.setId(1L);
        proceso.setCodigo("VDC-MP-001-P-001");
        proceso.setNombre("Proceso de Auditoría");
        proceso.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);
        proceso.setSubprocesos(new ArrayList<>());

        // Preparar DTO de solicitud
        requestDTO = new SubprocesoRequestDTO();
        requestDTO.setProcesoId(1L);
        requestDTO.setNombre("Planificación de Auditoría");
        requestDTO.setDescripcion("Subproceso de planificación");
        requestDTO.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);

        // Preparar entidad subproceso
        subproceso = new Subproceso();
        subproceso.setId(1L);
        subproceso.setCodigo("VDC-MP-001-P-001-SP-001");
        subproceso.setNombre("Planificación de Auditoría");
        subproceso.setDescripcion("Subproceso de planificación");
        subproceso.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);
        subproceso.setProceso(proceso);
        subproceso.setPorcentajeAvance(0);
        
        // Configurar proceso para evitar NullPointerException
        proceso.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);
        proceso.setMacroproceso(macroproceso);
    }

    @Test
    void testCrear_Exitoso() {
        // Arrange
        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
        lenient().when(subprocesoRepository.countByProcesoId(1L)).thenReturn(0L);
        lenient().when(subprocesoRepository.existsByCodigo(anyString())).thenReturn(false);
        when(subprocesoRepository.save(any(Subproceso.class))).thenReturn(subproceso);

        // Act
        SubprocesoResponseDTO resultado = subprocesoService.crear(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Planificación de Auditoría", resultado.getNombre());
        assertEquals(1L, resultado.getProcesoId());
        verify(subprocesoRepository, times(1)).save(any(Subproceso.class));
    }

    @Test
    void testCrear_ProcesoNoEncontrado() {
        // Arrange
        when(procesoRepository.findById(999L)).thenReturn(Optional.empty());
        requestDTO.setProcesoId(999L);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            subprocesoService.crear(requestDTO);
        });
        verify(subprocesoRepository, never()).save(any(Subproceso.class));
    }

    @Test
    void testObtenerTodos_RetornaLista() {
        // Arrange
        Subproceso subproceso2 = new Subproceso();
        subproceso2.setId(2L);
        subproceso2.setCodigo("VDC-MP-001-P-001-SP-002");
        subproceso2.setNombre("Ejecución de Auditoría");
        subproceso2.setProceso(proceso);
        
        when(subprocesoRepository.findAll()).thenReturn(Arrays.asList(subproceso, subproceso2));

        // Act
        List<SubprocesoResponseDTO> resultado = subprocesoService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void testObtenerPorId_Exitoso() {
        // Arrange
        when(subprocesoRepository.findById(1L)).thenReturn(Optional.of(subproceso));

        // Act
        SubprocesoResponseDTO resultado = subprocesoService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Planificación de Auditoría", resultado.getNombre());
    }

    @Test
    void testObtenerPorId_NoEncontrado() {
        // Arrange
        when(subprocesoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            subprocesoService.obtenerPorId(999L);
        });
    }

    @Test
    void testObtenerPorProceso_Exitoso() {
        // Arrange
        when(subprocesoRepository.findByProcesoId(1L)).thenReturn(Arrays.asList(subproceso));

        // Act
        List<SubprocesoResponseDTO> resultado = subprocesoService.obtenerPorProceso(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Planificación de Auditoría", resultado.get(0).getNombre());
    }

    @Test
    void testActualizar_Exitoso() {
        // Arrange
        when(subprocesoRepository.findById(1L)).thenReturn(Optional.of(subproceso));
        when(subprocesoRepository.save(any(Subproceso.class))).thenReturn(subproceso);

        requestDTO.setNombre("Subproceso Actualizado");

        // Act
        SubprocesoResponseDTO resultado = subprocesoService.actualizar(1L, requestDTO);

        // Assert
        assertNotNull(resultado);
        verify(subprocesoRepository, times(1)).save(any(Subproceso.class));
    }

    @Test
    void testActualizar_NoEncontrado() {
        // Arrange
        when(subprocesoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            subprocesoService.actualizar(999L, requestDTO);
        });
        verify(subprocesoRepository, never()).save(any(Subproceso.class));
    }

    @Test
    void testEliminar_Exitoso() {
        // Arrange
        when(subprocesoRepository.findById(1L)).thenReturn(Optional.of(subproceso));
        doNothing().when(subprocesoRepository).delete(any(Subproceso.class));

        // Act
        subprocesoService.eliminar(1L);

        // Assert
        verify(subprocesoRepository, times(1)).delete(subproceso);
    }

    @Test
    void testEliminar_NoEncontrado() {
        // Arrange
        when(subprocesoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            subprocesoService.eliminar(999L);
        });
        verify(subprocesoRepository, never()).delete(any(Subproceso.class));
    }

    @Test
    void testActualizarEstado_Exitoso() {
        // Arrange
        when(subprocesoRepository.findById(1L)).thenReturn(Optional.of(subproceso));
        when(subprocesoRepository.save(any(Subproceso.class))).thenReturn(subproceso);

        // Act
        SubprocesoResponseDTO resultado = subprocesoService.actualizarEstado(1L, EstadoDocumentacion.CARACTERIZACION);

        // Assert
        assertNotNull(resultado);
        verify(subprocesoRepository, times(1)).save(any(Subproceso.class));
    }

    @Test
    void testActualizarEstado_NoEncontrado() {
        // Arrange
        when(subprocesoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            subprocesoService.actualizarEstado(999L, EstadoDocumentacion.LEGALIZADO);
        });
    }

    @Test
    void testGenerarCodigo_Unico() {
        // Arrange
        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
        lenient().when(subprocesoRepository.countByProcesoId(1L)).thenReturn(10L);
        lenient().when(subprocesoRepository.existsByCodigo(anyString())).thenReturn(false);
        when(subprocesoRepository.save(any(Subproceso.class))).thenReturn(subproceso);

        // Act
        subprocesoService.crear(requestDTO);

        // Assert
        verify(subprocesoRepository).existsByCodigo(anyString());
    }

    @Test
    void testObtenerTodos_RetornaListaVacia() {
        // Arrange
        when(subprocesoRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<SubprocesoResponseDTO> resultado = subprocesoService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
