package ec.edu.espe.inventario.model.enums;

/**
 * Tipos de macroprocesos según estructura organizacional ESPE
 */
public enum TipoMacroproceso {
    GOBIERNO_DIRECCION("Gobierno o Dirección"),
    HABILITANTE_ASESORIA("Habilitante de Asesoría"),
    HABILITANTE_APOYO("Habilitante de Apoyo"),
    AGREGADOR_VALOR("Agregador de Valor");

    private final String descripcion;

    TipoMacroproceso(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}