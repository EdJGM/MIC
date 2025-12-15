package ec.edu.espe.inventario.model.enums;

/**
 * Estados de documentación de procesos según ERS
 */
public enum EstadoDocumentacion {
    NO_DOCUMENTADO("No documentado", 0),
    LEVANTAMIENTO("Levantamiento", 30),
    FLUJODIAGRAMACION("Flujodiagramación", 60),
    CARACTERIZACION("Caracterización", 60),
    VALIDACION("Validación", 75),
    LEGALIZADO("Legalizado", 90),
    DIFUNDIDO("Difundido", 100),
    MEJORA("Mejora", 100);
    
    private final String descripcion;
    private final int porcentajeAsociado;
    
    EstadoDocumentacion(String descripcion, int porcentajeAsociado) {
        this.descripcion = descripcion;
        this.porcentajeAsociado = porcentajeAsociado;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public int getPorcentajeAsociado() {
        return porcentajeAsociado;
    }
}
