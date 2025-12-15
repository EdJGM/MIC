package ec.edu.espe.inventario.repository;

import ec.edu.espe.inventario.model.entity.Macroproceso;
import ec.edu.espe.inventario.model.enums.EstadoDocumentacion;
import ec.edu.espe.inventario.model.enums.TipoMacroproceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Macroproceso
 */
@Repository
public interface MacroprocesoRepository extends JpaRepository<Macroproceso, Long> {
    
    Optional<Macroproceso> findByCodigo(String codigo);
    
    List<Macroproceso> findByTipo(TipoMacroproceso tipo);
    
    List<Macroproceso> findByEstadoDocumentacion(EstadoDocumentacion estado);
    
    List<Macroproceso> findByUnidadEstrategica(String unidadEstrategica);
    
    @Query("SELECT m FROM Macroproceso m LEFT JOIN FETCH m.procesos")
    List<Macroproceso> findAllWithProcesos();
    
    @Query("SELECT m FROM Macroproceso m LEFT JOIN FETCH m.procesos WHERE m.id = :id")
    Optional<Macroproceso> findByIdWithProcesos(Long id);
    
    boolean existsByCodigo(String codigo);
}
