package ec.edu.espe.inventario.model.enums;

/**
 * Tipo de dato del parámetro del sistema
 */
public enum TipoParametro {
    ENTERO("Numérico entero"),
    DECIMAL("Numérico decimal"),
    TEXTO("Texto"),
    BOOLEANO("Booleano"),
    FECHA("Fecha");

    private final String descripcion;

    TipoParametro(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}