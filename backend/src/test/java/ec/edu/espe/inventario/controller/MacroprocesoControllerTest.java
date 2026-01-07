package ec.edu.espe.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.inventario.model.dto.MacroprocesoRequestDTO;
import ec.edu.espe.inventario.model.dto.MacroprocesoResponseDTO;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.model.enums.TipoMacroproceso;
import ec.edu.espe.inventario.service.MacroprocesoService;
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
 * Pruebas unitarias para MacroprocesoController
 */
@WebMvcTest(MacroprocesoController.class)
class MacroprocesoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MacroprocesoService macroprocesoService;

    private MacroprocesoRequestDTO requestDTO;
    private MacroprocesoResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new MacroprocesoRequestDTO();
        requestDTO.setTipo(TipoMacroproceso.VDC);
        requestDTO.setNombre("Gestión de Calidad");
        requestDTO.setDescripcion("Proceso de gestión de calidad");
        requestDTO.setUnidadEstrategica("VDC");
        requestDTO.setResponsablePrincipal("Director de Calidad");
        requestDTO.setObjetivosEstrategicos("Mejorar calidad");
        requestDTO.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);

        responseDTO = new MacroprocesoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCodigo("VDC-MP-001");
        responseDTO.setTipo(TipoMacroproceso.VDC);
        responseDTO.setNombre("Gestión de Calidad");
        responseDTO.setDescripcion("Proceso de gestión de calidad");
        responseDTO.setUnidadEstrategica("VDC");
        responseDTO.setResponsablePrincipal("Director de Calidad");
        responseDTO.setObjetivosEstrategicos("Mejorar calidad");
        responseDTO.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);
        responseDTO.setPorcentajeAvance(0);
        responseDTO.setCantidadProcesos(0);
        responseDTO.setFechaCreacion(LocalDateTime.now());
        responseDTO.setFechaActualizacion(LocalDateTime.now());
    }

    @Test
    void testCrear_Exitoso() throws Exception {
        // Arrange
        when(macroprocesoService.crear(any(MacroprocesoRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/macroprocesos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigo", is("VDC-MP-001")))
                .andExpect(jsonPath("$.nombre", is("Gestión de Calidad")))
                .andExpect(jsonPath("$.tipo", is("VDC")));

        verify(macroprocesoService, times(1)).crear(any(MacroprocesoRequestDTO.class));
    }

    @Test
    void testObtenerTodos_Exitoso() throws Exception {
        // Arrange
        MacroprocesoResponseDTO responseDTO2 = new MacroprocesoResponseDTO();
        responseDTO2.setId(2L);
        responseDTO2.setCodigo("UTIC-MP-001");
        responseDTO2.setNombre("Gestión de TIC");
        
        List<MacroprocesoResponseDTO> lista = Arrays.asList(responseDTO, responseDTO2);
        when(macroprocesoService.obtenerTodos()).thenReturn(lista);

        // Act & Assert
        mockMvc.perform(get("/api/macroprocesos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].codigo", is("VDC-MP-001")))
                .andExpect(jsonPath("$[1].codigo", is("UTIC-MP-001")));

        verify(macroprocesoService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerPorId_Exitoso() throws Exception {
        // Arrange
        when(macroprocesoService.obtenerPorId(1L)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/macroprocesos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigo", is("VDC-MP-001")))
                .andExpect(jsonPath("$.nombre", is("Gestión de Calidad")));

        verify(macroprocesoService, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerPorId_NoEncontrado() throws Exception {
        // Arrange
        when(macroprocesoService.obtenerPorId(999L))
                .thenThrow(new RuntimeException("Macroproceso no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/macroprocesos/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizar_Exitoso() throws Exception {
        // Arrange
        requestDTO.setNombre("Gestión de Calidad Actualizado");
        responseDTO.setNombre("Gestión de Calidad Actualizado");
        
        when(macroprocesoService.actualizar(eq(1L), any(MacroprocesoRequestDTO.class)))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/macroprocesos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Gestión de Calidad Actualizado")));

        verify(macroprocesoService, times(1)).actualizar(eq(1L), any(MacroprocesoRequestDTO.class));
    }

    @Test
    void testEliminar_Exitoso() throws Exception {
        // Arrange
        doNothing().when(macroprocesoService).eliminar(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/macroprocesos/1"))
                .andExpect(status().isNoContent());

        verify(macroprocesoService, times(1)).eliminar(1L);
    }

    @Test
    void testEliminar_ConProcesosAsociados() throws Exception {
        // Arrange
        doThrow(new RuntimeException("No se puede eliminar el macroproceso porque tiene procesos asociados"))
                .when(macroprocesoService).eliminar(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/macroprocesos/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarEstado_Exitoso() throws Exception {
        // Arrange
        responseDTO.setEstadoDocumentacion(EstadoDocumentacion.LEVANTAMIENTO);
        when(macroprocesoService.actualizarEstado(1L, EstadoDocumentacion.LEVANTAMIENTO))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/macroprocesos/1/estado")
                .param("nuevoEstado", "LEVANTAMIENTO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoDocumentacion", is("LEVANTAMIENTO")));

        verify(macroprocesoService, times(1))
                .actualizarEstado(1L, EstadoDocumentacion.LEVANTAMIENTO);
    }

    @Test
    void testCrear_ConDatosInvalidos() throws Exception {
        // Arrange
        requestDTO.setNombre(""); // Nombre vacío (inválido)

        // Act & Assert
        mockMvc.perform(post("/api/macroprocesos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        verify(macroprocesoService, never()).crear(any(MacroprocesoRequestDTO.class));
    }

    @Test
    void testActualizar_NoEncontrado() throws Exception {
        // Arrange
        when(macroprocesoService.actualizar(eq(999L), any(MacroprocesoRequestDTO.class)))
                .thenThrow(new RuntimeException("Macroproceso no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/macroprocesos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}
