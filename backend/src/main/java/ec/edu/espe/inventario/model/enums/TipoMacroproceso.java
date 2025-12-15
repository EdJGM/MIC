package ec.edu.espe.inventario.model.enums;

/**
 * Tipos de macroprocesos según estructura organizacional ESPE
 */
public enum TipoMacroproceso {
    REC("Procesos de Realización"),
    UTIC("Procesos de Apoyo - UTIC"),
    USGN("Procesos Gobernantes"),
    VDC("Vicerrectorado de Docencia"),
    VAD("Vicerrectorado de Administración"),
    VAG("Vicerrectorado Académico General"),
    VII("Vicerrectorado de Innovación e Investigación");
    
    private final String descripcion;
    
    TipoMacroproceso(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
