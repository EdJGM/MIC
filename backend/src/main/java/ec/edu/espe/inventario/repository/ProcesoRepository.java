package ec.edu.espe.inventario.repository;

import ec.edu.espe.inventario.model.entity.Proceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Proceso
 */
@Repository
public interface ProcesoRepository extends JpaRepository<Proceso, Long> {

    Optional<Proceso> findByCodigo(String codigo);

    List<Proceso> findByMacroprocesoId(Long macroprocesoId);

    List<Proceso> findByMacroprocesoIdAndNivel(Long macroprocesoId, int nivel);

    /** Procesos N2 hijos de un Proceso N1 */
    List<Proceso> findByProcesoPadreId(Long procesoPadreId);

    Long countByProcesoPadreId(Long procesoPadreId);

    List<Proceso> findByEstadoDocumentacion(EstadoDocumentacion estado);

    @Query("SELECT p FROM Proceso p LEFT JOIN FETCH p.subprocesos WHERE p.macroproceso.id = :macroprocesoId")
    List<Proceso> findByMacroprocesoIdWithSubprocesos(Long macroprocesoId);

    @Query("SELECT p FROM Proceso p LEFT JOIN FETCH p.subprocesos WHERE p.id = :id")
    Optional<Proceso> findByIdWithSubprocesos(Long id);

    boolean existsByCodigo(String codigo);

    Long countByMacroprocesoId(Long macroprocesoId);

    Long countByMacroprocesoIdAndNivel(Long macroprocesoId, int nivel);
}
