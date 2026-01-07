package ec.edu.espe.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.inventario.model.dto.SubprocesoRequestDTO;
import ec.edu.espe.inventario.model.dto.SubprocesoResponseDTO;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.service.SubprocesoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para SubprocesoController
 */
@WebMvcTest(SubprocesoController.class)
class SubprocesoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubprocesoService subprocesoService;

    private SubprocesoRequestDTO requestDTO;
    private SubprocesoResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new SubprocesoRequestDTO();
        requestDTO.setProcesoId(1L);
        requestDTO.setNombre("Planificación de Auditoría");
        requestDTO.setDescripcion("Subproceso de planificación");
        requestDTO.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);

        responseDTO = new SubprocesoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCodigo("VDC-MP-001-P-001-SP-001");
        responseDTO.setProcesoId(1L);
        responseDTO.setNombre("Planificación de Auditoría");
        responseDTO.setDescripcion("Subproceso de planificación");
        responseDTO.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);
        responseDTO.setPorcentajeAvance(0);
        responseDTO.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void testCrear_Exitoso() throws Exception {
        // Arrange
        when(subprocesoService.crear(any(SubprocesoRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/subprocesos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigo", is("VDC-MP-001-P-001-SP-001")))
                .andExpect(jsonPath("$.nombre", is("Planificación de Auditoría")))
                .andExpect(jsonPath("$.procesoId", is(1)));

        verify(subprocesoService, times(1)).crear(any(SubprocesoRequestDTO.class));
    }

    @Test
    void testObtenerTodos_Exitoso() throws Exception {
        // Arrange
        SubprocesoResponseDTO responseDTO2 = new SubprocesoResponseDTO();
        responseDTO2.setId(2L);
        responseDTO2.setCodigo("VDC-MP-001-P-001-SP-002");
        responseDTO2.setNombre("Ejecución de Auditoría");
        
        List<SubprocesoResponseDTO> lista = Arrays.asList(responseDTO, responseDTO2);
        when(subprocesoService.obtenerTodos()).thenReturn(lista);

        // Act & Assert
        mockMvc.perform(get("/api/subprocesos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].codigo", is("VDC-MP-001-P-001-SP-001")))
                .andExpect(jsonPath("$[1].codigo", is("VDC-MP-001-P-001-SP-002")));

        verify(subprocesoService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerPorId_Exitoso() throws Exception {
        // Arrange
        when(subprocesoService.obtenerPorId(1L)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/subprocesos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigo", is("VDC-MP-001-P-001-SP-001")))
                .andExpect(jsonPath("$.nombre", is("Planificación de Auditoría")));

        verify(subprocesoService, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerPorProceso_Exitoso() throws Exception {
        // Arrange
        when(subprocesoService.obtenerPorProceso(1L)).thenReturn(Arrays.asList(responseDTO));

        // Act & Assert
        mockMvc.perform(get("/api/subprocesos/proceso/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].procesoId", is(1)));

        verify(subprocesoService, times(1)).obtenerPorProceso(1L);
    }

    @Test
    void testActualizar_Exitoso() throws Exception {
        // Arrange
        requestDTO.setNombre("Subproceso Actualizado");
        responseDTO.setNombre("Subproceso Actualizado");
        
        when(subprocesoService.actualizar(eq(1L), any(SubprocesoRequestDTO.class)))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/subprocesos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Subproceso Actualizado")));

        verify(subprocesoService, times(1)).actualizar(eq(1L), any(SubprocesoRequestDTO.class));
    }

    @Test
    void testEliminar_Exitoso() throws Exception {
        // Arrange
        doNothing().when(subprocesoService).eliminar(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/subprocesos/1"))
                .andExpect(status().isNoContent());

        verify(subprocesoService, times(1)).eliminar(1L);
    }

    @Test
    void testEliminar_NoEncontrado() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Subproceso no encontrado con ID: 999"))
                .when(subprocesoService).eliminar(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/subprocesos/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarEstado_Exitoso() throws Exception {
        // Arrange
        responseDTO.setEstadoDocumentacion(EstadoDocumentacion.VALIDACION);
        when(subprocesoService.actualizarEstado(1L, EstadoDocumentacion.VALIDACION))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/subprocesos/1/estado")
                .param("nuevoEstado", "VALIDACION"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoDocumentacion", is("VALIDACION")));

        verify(subprocesoService, times(1))
                .actualizarEstado(1L, EstadoDocumentacion.VALIDACION);
    }

    @Test
    void testCrear_ConDatosInvalidos() throws Exception {
        // Arrange
        requestDTO.setNombre(""); // Nombre vacío (inválido)

        // Act & Assert
        mockMvc.perform(post("/api/subprocesos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        verify(subprocesoService, never()).crear(any(SubprocesoRequestDTO.class));
    }

    @Test
    void testObtenerPorId_NoEncontrado() throws Exception {
        // Arrange
        when(subprocesoService.obtenerPorId(999L))
                .thenThrow(new RuntimeException("Subproceso no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/subprocesos/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizar_NoEncontrado() throws Exception {
        // Arrange
        when(subprocesoService.actualizar(eq(999L), any(SubprocesoRequestDTO.class)))
                .thenThrow(new RuntimeException("Subproceso no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/subprocesos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testObtenerPorProceso_ListaVacia() throws Exception {
        // Arrange
        when(subprocesoService.obtenerPorProceso(999L)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/subprocesos/proceso/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(subprocesoService, times(1)).obtenerPorProceso(999L);
    }
}
