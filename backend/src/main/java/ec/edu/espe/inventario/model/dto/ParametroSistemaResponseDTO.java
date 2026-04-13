package ec.edu.espe.inventario.model.dto;

import ec.edu.espe.inventario.model.enums.TipoParametro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para ParametroSistema
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametroSistemaResponseDTO {

    private Long id;
    private String clave;
    private String valor;
    private String descripcion;
    private TipoParametro tipo;
    private LocalDateTime fechaActualizacion;
    private String modificadoPor;
}
