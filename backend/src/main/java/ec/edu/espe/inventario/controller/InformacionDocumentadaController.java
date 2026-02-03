package ec.edu.espe.inventario.controller;

import ec.edu.espe.inventario.model.dto.InformacionDocumentadaRequestDTO;
import ec.edu.espe.inventario.model.dto.InformacionDocumentadaResponseDTO;
import ec.edu.espe.inventario.service.InformacionDocumentadaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/informacion-documentada")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class InformacionDocumentadaController {

    private final InformacionDocumentadaService informacionDocumentadaService;

    @PostMapping
    public ResponseEntity<InformacionDocumentadaResponseDTO> crear(@RequestBody InformacionDocumentadaRequestDTO requestDTO) {
        try {
            InformacionDocumentadaResponseDTO responseDTO = informacionDocumentadaService.crear(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<InformacionDocumentadaResponseDTO>> listarTodos() {
        try {
            List<InformacionDocumentadaResponseDTO> lista = informacionDocumentadaService.listarTodos();
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sede/{sede}")
    public ResponseEntity<List<InformacionDocumentadaResponseDTO>> listarPorSede(@PathVariable String sede) {
        try {
            List<InformacionDocumentadaResponseDTO> lista = informacionDocumentadaService.listarPorSede(sede);
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<InformacionDocumentadaResponseDTO> obtenerPorId(@PathVariable Long id) {
        try {
            InformacionDocumentadaResponseDTO responseDTO = informacionDocumentadaService.obtenerPorId(id);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<InformacionDocumentadaResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody InformacionDocumentadaRequestDTO requestDTO) {
        try {
            InformacionDocumentadaResponseDTO responseDTO = informacionDocumentadaService.actualizar(id, requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            informacionDocumentadaService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
