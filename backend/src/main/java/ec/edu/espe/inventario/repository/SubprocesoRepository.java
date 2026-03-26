package ec.edu.espe.inventario.repository;

import ec.edu.espe.inventario.model.entity.Subproceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Subproceso
 */
@Repository
public interface SubprocesoRepository extends JpaRepository<Subproceso, Long> {

    Optional<Subproceso> findByCodigo(String codigo);

    List<Subproceso> findByProcesoId(Long procesoId);

    List<Subproceso> findByProcesoIdAndNivel(Long procesoId, int nivel);

    /** Subprocesos N2 hijos de un Subproceso N1 */
    List<Subproceso> findBySubprocesoPadreId(Long subprocesoPadreId);

    Long countBySubprocesoPadreId(Long subprocesoPadreId);

    List<Subproceso> findByEstadoDocumentacion(EstadoDocumentacion estado);

    boolean existsByCodigo(String codigo);

    Long countByProcesoId(Long procesoId);
}
