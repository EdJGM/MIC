package ec.edu.espe.inventario.repository;

import ec.edu.espe.inventario.model.entity.UsuarioLocal;
import ec.edu.espe.inventario.model.enums.RolLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para UsuarioLocal
 */
@Repository
public interface UsuarioLocalRepository extends JpaRepository<UsuarioLocal, Long> {

    Optional<UsuarioLocal> findByExternalId(String externalId);

    Optional<UsuarioLocal> findByEmail(String email);

    List<UsuarioLocal> findByRolLocal(RolLocal rolLocal);

    List<UsuarioLocal> findByUnidadAsignada(String unidadAsignada);

    List<UsuarioLocal> findByActivo(Boolean activo);

    boolean existsByExternalId(String externalId);

    boolean existsByEmail(String email);
}
