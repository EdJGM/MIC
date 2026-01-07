package ec.edu.espe.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.espe.inventario.model.dto.ProcesoRequestDTO;
import ec.edu.espe.inventario.model.dto.ProcesoResponseDTO;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.service.ProcesoService;
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
 * Pruebas unitarias para ProcesoController
 */
@WebMvcTest(ProcesoController.class)
class ProcesoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProcesoService procesoService;

    private ProcesoRequestDTO requestDTO;
    private ProcesoResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new ProcesoRequestDTO();
        requestDTO.setMacroprocesoId(1L);
        requestDTO.setNombre("Proceso de Auditoría");
        requestDTO.setDescripcion("Proceso de auditoría interna");
        requestDTO.setObjetivos("Realizar auditorías internas");
        requestDTO.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);

        responseDTO = new ProcesoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCodigo("VDC-MP-001-P-001");
        responseDTO.setMacroprocesoId(1L);
        responseDTO.setNombre("Proceso de Auditoría");
        responseDTO.setDescripcion("Proceso de auditoría interna");
        responseDTO.setObjetivos("Realizar auditorías internas");
        responseDTO.setEstadoDocumentacion(EstadoDocumentacion.NO_DOCUMENTADO);
        responseDTO.setPorcentajeAvance(0);
        responseDTO.setCantidadSubprocesos(0);
        responseDTO.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void testCrear_Exitoso() throws Exception {
        // Arrange
        when(procesoService.crear(any(ProcesoRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/procesos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigo", is("VDC-MP-001-P-001")))
                .andExpect(jsonPath("$.nombre", is("Proceso de Auditoría")))
                .andExpect(jsonPath("$.macroprocesoId", is(1)));

        verify(procesoService, times(1)).crear(any(ProcesoRequestDTO.class));
    }

    @Test
    void testObtenerTodos_Exitoso() throws Exception {
        // Arrange
        ProcesoResponseDTO responseDTO2 = new ProcesoResponseDTO();
        responseDTO2.setId(2L);
        responseDTO2.setCodigo("VDC-MP-001-P-002");
        responseDTO2.setNombre("Proceso de Evaluación");
        
        List<ProcesoResponseDTO> lista = Arrays.asList(responseDTO, responseDTO2);
        when(procesoService.obtenerTodos()).thenReturn(lista);

        // Act & Assert
        mockMvc.perform(get("/api/procesos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].codigo", is("VDC-MP-001-P-001")))
                .andExpect(jsonPath("$[1].codigo", is("VDC-MP-001-P-002")));

        verify(procesoService, times(1)).obtenerTodos();
    }

    @Test
    void testObtenerPorId_Exitoso() throws Exception {
        // Arrange
        when(procesoService.obtenerPorId(1L)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/procesos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigo", is("VDC-MP-001-P-001")))
                .andExpect(jsonPath("$.nombre", is("Proceso de Auditoría")));

        verify(procesoService, times(1)).obtenerPorId(1L);
    }

    @Test
    void testObtenerPorMacroproceso_Exitoso() throws Exception {
        // Arrange
        when(procesoService.obtenerPorMacroproceso(1L)).thenReturn(Arrays.asList(responseDTO));

        // Act & Assert
        mockMvc.perform(get("/api/procesos/macroproceso/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].macroprocesoId", is(1)));

        verify(procesoService, times(1)).obtenerPorMacroproceso(1L);
    }

    @Test
    void testActualizar_Exitoso() throws Exception {
        // Arrange
        requestDTO.setNombre("Proceso Actualizado");
        responseDTO.setNombre("Proceso Actualizado");
        
        when(procesoService.actualizar(eq(1L), any(ProcesoRequestDTO.class)))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/procesos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Proceso Actualizado")));

        verify(procesoService, times(1)).actualizar(eq(1L), any(ProcesoRequestDTO.class));
    }

    @Test
    void testEliminar_Exitoso() throws Exception {
        // Arrange
        doNothing().when(procesoService).eliminar(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/procesos/1"))
                .andExpect(status().isNoContent());

        verify(procesoService, times(1)).eliminar(1L);
    }

    @Test
    void testEliminar_ConSubprocesosAsociados() throws Exception {
        // Arrange
        doThrow(new RuntimeException("No se puede eliminar el proceso porque tiene subprocesos asociados"))
                .when(procesoService).eliminar(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/procesos/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarEstado_Exitoso() throws Exception {
        // Arrange
        responseDTO.setEstadoDocumentacion(EstadoDocumentacion.CARACTERIZACION);
        when(procesoService.actualizarEstado(1L, EstadoDocumentacion.CARACTERIZACION))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/procesos/1/estado")
                .param("nuevoEstado", "CARACTERIZACION"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoDocumentacion", is("CARACTERIZACION")));

        verify(procesoService, times(1))
                .actualizarEstado(1L, EstadoDocumentacion.CARACTERIZACION);
    }

    @Test
    void testCrear_ConDatosInvalidos() throws Exception {
        // Arrange
        requestDTO.setNombre(""); // Nombre vacío (inválido)

        // Act & Assert
        mockMvc.perform(post("/api/procesos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        verify(procesoService, never()).crear(any(ProcesoRequestDTO.class));
    }

    @Test
    void testObtenerPorId_NoEncontrado() throws Exception {
        // Arrange
        when(procesoService.obtenerPorId(999L))
                .thenThrow(new RuntimeException("Proceso no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/procesos/999"))
                .andExpect(status().isBadRequest());
    }
}
