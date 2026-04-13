package ec.edu.espe.inventario.repository;

import ec.edu.espe.inventario.model.entity.ParametroSistema;
import ec.edu.espe.inventario.model.enums.TipoParametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para ParametroSistema
 */
@Repository
public interface ParametroSistemaRepository extends JpaRepository<ParametroSistema, Long> {

    Optional<ParametroSistema> findByClave(String clave);

    List<ParametroSistema> findByTipo(TipoParametro tipo);

    boolean existsByClave(String clave);
}
