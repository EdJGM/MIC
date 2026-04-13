package ec.edu.espe.inventario.repository;

import ec.edu.espe.inventario.model.entity.InformacionDocumentada;
import ec.edu.espe.inventario.model.enums.EstadoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InformacionDocumentadaRepository extends JpaRepository<InformacionDocumentada, Long> {
    
    List<InformacionDocumentada> findByEstadoOrderByFechaSolicitudDesc(EstadoDocumento estado);
    
    List<InformacionDocumentada> findBySedeAndEstadoOrderByFechaSolicitudDesc(String sede, EstadoDocumento estado);
    
    List<InformacionDocumentada> findByUnidadAndEstadoOrderByFechaSolicitudDesc(String unidad, EstadoDocumento estado);
    
    List<InformacionDocumentada> findByMacroprocesoIdAndEstadoOrderByFechaSolicitudDesc(Long macroprocesoId, EstadoDocumento estado);
    
    Optional<InformacionDocumentada> findByCodigoDocumento(String codigoDocumento);
    
    @Query("SELECT i FROM InformacionDocumentada i WHERE i.estado = :estado AND i.sede = :sede ORDER BY i.fechaSolicitud DESC")
    List<InformacionDocumentada> findBySede(@Param("sede") String sede, @Param("estado") EstadoDocumento estado);
    
    @Query("SELECT MAX(i.secuencial) FROM InformacionDocumentada i WHERE i.anio = :anio AND i.tipoDocumento = :tipoDocumento AND i.unidad = :unidad")
    Integer findMaxSecuencialByAnioAndTipoDocumentoAndUnidad(@Param("anio") Integer anio, @Param("tipoDocumento") String tipoDocumento, @Param("unidad") String unidad);
    
    @Query("SELECT i FROM InformacionDocumentada i WHERE i.estado = :estado ORDER BY i.mes ASC, i.codigoDocumento ASC")
    List<InformacionDocumentada> findAllActivosOrdenados(@Param("estado") EstadoDocumento estado);

    @Query("SELECT i FROM InformacionDocumentada i WHERE i.estado = 'ACTIVO' AND i.fechaProtocolo IS NOT NULL AND i.fechaProtocolo BETWEEN :desde AND :hasta")
    List<InformacionDocumentada> findActivosConFechaProtocoloEntre(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    List<InformacionDocumentada> findByDocumentoOrigenIdOrderByCreatedAtDesc(Long documentoOrigenId);

    @Query("SELECT i FROM InformacionDocumentada i WHERE (i.id = :origenId OR i.documentoOrigenId = :origenId) ORDER BY i.createdAt DESC")
    List<InformacionDocumentada> findHistorialByOrigenId(@Param("origenId") Long origenId);
}
