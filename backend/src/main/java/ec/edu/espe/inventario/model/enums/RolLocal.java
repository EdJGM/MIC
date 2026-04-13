package ec.edu.espe.inventario.model.enums;

/**
 * Roles locales del sistema según ERS (sección 3.3.4)
 */
public enum RolLocal {
    ADMINISTRADOR("Administrador", "Acceso completo al sistema"),
    COORDINADOR_UPDI("Coordinador UPDI", "Coordinación de documentos normativos e inventario de procesos"),
    RESPONSABLE_UNIDAD("Responsable de Unidad", "Gestión de documentos y procesos de su unidad organizacional"),
    REVISOR("Revisor", "Revisión y observación de documentos asignados"),
    CONSULTOR("Consultor", "Solo lectura sobre documentos y procesos");

    private final String nombre;
    private final String descripcion;

    RolLocal(String nombre, String descripcion) {
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