package ec.edu.espe.inventario.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NuevaVersionRequestDTO {
    private String enlaceArchivo;
    private String observaciones;
}
