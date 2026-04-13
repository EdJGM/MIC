package ec.edu.espe.inventario.model.enums;

/**
 * Tipos de notificación del sistema según ERS (sección 3.3.5 - RF-38)
 */
public enum TipoNotificacion {
    DOCUMENTO_VENCIDO("Documento vencido", "El documento ha superado su fecha de vigencia"),
    DOCUMENTO_PROXIMO_VENCER("Próximo a vencer", "El documento está próximo a su fecha de vigencia"),
    PROCESO_PENDIENTE("Proceso pendiente", "Proceso con retraso en documentación"),
    SOLICITUD_REVISION("Solicitud de revisión", "Documento enviado para revisión"),
    APROBACION_REQUERIDA("Aprobación requerida", "Documento pendiente de aprobación"),
    DOCUMENTO_APROBADO("Documento aprobado", "El documento fue aprobado"),
    DOCUMENTO_RECHAZADO("Documento rechazado", "El documento fue observado o rechazado"),
    SISTEMA("Sistema", "Notificación general del sistema");

    private final String nombre;
    private final String descripcion;

    TipoNotificacion(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}