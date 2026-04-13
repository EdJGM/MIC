package ec.edu.espe.inventario.model.entity;

import ec.edu.espe.inventario.model.enums.RolLocal;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Usuario local del sistema — vincula un usuario externo (universidad) con un rol
 * dentro de esta aplicación. No almacena contraseñas: la autenticación la gestiona
 * la universidad. El campo externalId se conectará con el token universitario.
 */
@Entity
@Table(name = "usuarios_locales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLocal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID del usuario en el sistema universitario.
     * Temporal: mientras no haya token se usa el email como identificador.
     * Cuando llegue la auth universitaria, este campo se mapea al sub/id del token.
     */
    @Column(unique = true, nullable = false, length = 150)
    @NotBlank(message = "El ID externo es obligatorio")
    private String externalId;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    @Column(nullable = false, unique = true, length = 200)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false)
    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    private RolLocal rolLocal;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "La unidad asignada es obligatoria")
    private String unidadAsignada;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(nullable = false, length = 100)
    private String creadoPor;

    @Column(nullable = false, length = 100)
    private String actualizadoPor;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
