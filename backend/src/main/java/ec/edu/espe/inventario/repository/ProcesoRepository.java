package ec.edu.espe.inventario.repository;

import ec.edu.espe.inventario.model.entity.Proceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    // RF-29
    Long countByNivel(int nivel);

    // RF-28/30 — filtros por unidad y fecha
    @Query("SELECT p FROM Proceso p WHERE p.macroproceso.unidadEstrategica = :unidad")
    List<Proceso> findByUnidadEstrategica(@Param("unidad") String unidad);

    @Query("SELECT p FROM Proceso p WHERE p.fechaCreacion >= :desde AND p.fechaCreacion <= :hasta")
    List<Proceso> findByFechaCreacionBetween(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);

    @Query("SELECT p FROM Proceso p WHERE p.macroproceso.unidadEstrategica = :unidad AND p.fechaCreacion >= :desde AND p.fechaCreacion <= :hasta")
    List<Proceso> findByUnidadEstrategicaAndFecha(@Param("unidad") String unidad, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);
}
