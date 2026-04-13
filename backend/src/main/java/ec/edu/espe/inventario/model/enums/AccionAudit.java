package ec.edu.espe.inventario.model.enums;

/**
 * Acciones registradas en el log de auditoría según ERS (sección 3.3.4 - RF-36)
 */
public enum AccionAudit {
    CREAR("Crear"),
    ACTUALIZAR("Actualizar"),
    ELIMINAR("Eliminar"),
    CONSULTAR("Consultar"),
    LOGIN("Iniciar sesión"),
    LOGOUT("Cerrar sesión"),
    EXPORTAR("Exportar"),
    CAMBIO_ESTADO("Cambio de estado");

    private final String descripcion;

    AccionAudit(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}