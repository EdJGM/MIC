package ec.edu.espe.inventario.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import ec.edu.espe.inventario.model.dto.MacroprocesoRequestDTO;
import ec.edu.espe.inventario.model.dto.MacroprocesoResponseDTO;
import ec.edu.espe.inventario.model.entity.Macroproceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.model.enums.TipoMacroproceso;
import ec.edu.espe.inventario.repository.MacroprocesoRepository;

/**
 * Pruebas unitarias para MacroprocesoService
 */
@ExtendWith(MockitoExtension.class)
class MacroprocesoServiceTest {

    @Mock
    private MacroprocesoRepository macroprocesoRepository;

    @Mock
    private ProcesoService procesoService;

    @InjectMocks
    private MacroprocesoService macroprocesoService;

    private MacroprocesoRequestDTO requestDTO;
    private Macroproceso macroproceso;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
        requestDTO = new MacroprocesoRequestDTO();
        requestDTO.setTipo(TipoMacroproceso.VDC);
        requestDTO.setNombre("Gestión de Calidad");
        requestDTO.setDescripcion("Proceso de gestión de calidad institucional");
        requestDTO.setUnidadEstrategica("VDC");
        requestDTO.setResponsablePrincipal("Director de Calidad");
        requestDTO.setObjetivosEstrategicos("Mejorar la calidad institucional");
        requestDTO.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);

        macroproceso = new Macroproceso();
        macroproceso.setId(1L);
        macroproceso.setCodigo("VDC-MP-001");
        macroproceso.setTipo(TipoMacroproceso.VDC);
        macroproceso.setNombre("Gestión de Calidad");
        macroproceso.setDescripcion("Proceso de gestión de calidad institucional");
        macroproceso.setUnidadEstrategica("VDC");
        macroproceso.setResponsablePrincipal("Director de Calidad");
        macroproceso.setObjetivosEstrategicos("Mejorar la calidad institucional");
        macroproceso.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);
        macroproceso.setPorcentajeAvance(0);
        macroproceso.setProcesos(new ArrayList<>());
    }

    @Test
    void testCrearMacroproceso_Exitoso() {
        // Arrange
        when(macroprocesoRepository.count()).thenReturn(0L);
        when(macroprocesoRepository.existsByCodigo(anyString())).thenReturn(false);
        when(macroprocesoRepository.save(any(Macroproceso.class))).thenReturn(macroproceso);

        // Act
        MacroprocesoResponseDTO resultado = macroprocesoService.crear(requestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("VDC-MP-001", resultado.getCodigo());
        assertEquals("Gestión de Calidad", resultado.getNombre());
        assertEquals(TipoMacroproceso.VDC, resultado.getTipo());
        assertEquals(0, resultado.getPorcentajeAvance());
        verify(macroprocesoRepository, times(1)).save(any(Macroproceso.class));
    }

    @Test
    void testCrearMacroproceso_GeneraCodigoUnico() {
        // Arrange
        when(macroprocesoRepository.count()).thenReturn(5L);
        when(macroprocesoRepository.existsByCodigo("VDC-MP-006")).thenReturn(false);
        when(macroprocesoRepository.save(any(Macroproceso.class))).thenReturn(macroproceso);

        // Act
        MacroprocesoResponseDTO resultado = macroprocesoService.crear(requestDTO);

        // Assert
        assertNotNull(resultado);
        verify(macroprocesoRepository).existsByCodigo(anyString());
    }

    @Test
    void testObtenerTodos_RetornaListaVacia() {
        // Arrange
        when(macroprocesoRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<MacroprocesoResponseDTO> resultado = macroprocesoService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(macroprocesoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerTodos_RetornaLista() {
        // Arrange
        Macroproceso mp2 = new Macroproceso();
        mp2.setId(2L);
        mp2.setCodigo("UTIC-MP-001");
        mp2.setNombre("Gestión de TIC");
        mp2.setProcesos(new ArrayList<>());
        
        when(macroprocesoRepository.findAll()).thenReturn(Arrays.asList(macroproceso, mp2));

        // Act
        List<MacroprocesoResponseDTO> resultado = macroprocesoService.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("VDC-MP-001", resultado.get(0).getCodigo());
        assertEquals("UTIC-MP-001", resultado.get(1).getCodigo());
    }

    @Test
    void testObtenerPorId_Exitoso() {
        // Arrange
        when(macroprocesoRepository.findByIdWithProcesos(1L)).thenReturn(Optional.of(macroproceso));

        // Act
        MacroprocesoResponseDTO resultado = macroprocesoService.obtenerPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("VDC-MP-001", resultado.getCodigo());
        verify(macroprocesoRepository, times(1)).findByIdWithProcesos(1L);
    }

    @Test
    void testObtenerPorId_NoEncontrado() {
        // Arrange
        when(macroprocesoRepository.findByIdWithProcesos(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            macroprocesoService.obtenerPorId(999L);
        });
    }

    @Test
    void testActualizar_Exitoso() {
        // Arrange
        when(macroprocesoRepository.findById(1L)).thenReturn(Optional.of(macroproceso));
        when(macroprocesoRepository.save(any(Macroproceso.class))).thenReturn(macroproceso);

        requestDTO.setNombre("Gestión de Calidad Actualizado");
        requestDTO.setDescripcion("Nueva descripción");

        // Act
        MacroprocesoResponseDTO resultado = macroprocesoService.actualizar(1L, requestDTO);

        // Assert
        assertNotNull(resultado);
        verify(macroprocesoRepository, times(1)).findById(1L);
        verify(macroprocesoRepository, times(1)).save(any(Macroproceso.class));
    }

    @Test
    void testActualizar_NoEncontrado() {
        // Arrange
        when(macroprocesoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            macroprocesoService.actualizar(999L, requestDTO);
        });
        verify(macroprocesoRepository, never()).save(any(Macroproceso.class));
    }

    @Test
    void testEliminar_Exitoso() {
        // Arrange
        when(macroprocesoRepository.findById(1L)).thenReturn(Optional.of(macroproceso));
        doNothing().when(macroprocesoRepository).delete(any(Macroproceso.class));

        // Act
        macroprocesoService.eliminar(1L);

        // Assert
        verify(macroprocesoRepository, times(1)).findById(1L);
        verify(macroprocesoRepository, times(1)).delete(macroproceso);
    }

    @Test
    void testEliminar_ConProcesosAsociados_LanzaExcepcion() {
        // Arrange
        macroproceso.getProcesos().add(new ec.edu.espe.inventario.model.entity.Proceso());
        when(macroprocesoRepository.findById(1L)).thenReturn(Optional.of(macroproceso));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            macroprocesoService.eliminar(1L);
        });
        
        assertTrue(exception.getMessage().contains("tiene procesos asociados"));
        verify(macroprocesoRepository, never()).delete(any(Macroproceso.class));
    }

    @Test
    void testEliminar_NoEncontrado() {
        // Arrange
        when(macroprocesoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            macroprocesoService.eliminar(999L);
        });
        verify(macroprocesoRepository, never()).delete(any(Macroproceso.class));
    }

    @Test
    void testActualizarEstado_Exitoso() {
        // Arrange
        when(macroprocesoRepository.findById(1L)).thenReturn(Optional.of(macroproceso));
        when(macroprocesoRepository.save(any(Macroproceso.class))).thenReturn(macroproceso);

        // Act
        MacroprocesoResponseDTO resultado = macroprocesoService.actualizarEstado(1L, EstadoDocumentacion.LEVANTAMIENTO);

        // Assert
        assertNotNull(resultado);
        verify(macroprocesoRepository, times(1)).findById(1L);
        verify(macroprocesoRepository, times(1)).save(any(Macroproceso.class));
    }

    @Test
    void testActualizarEstado_NoEncontrado() {
        // Arrange
        when(macroprocesoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            macroprocesoService.actualizarEstado(999L, EstadoDocumentacion.LEGALIZADO);
        });
    }
}
