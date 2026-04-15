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

    // RF-24: contar por estado
    @Query("SELECT COUNT(i) FROM InformacionDocumentada i WHERE CAST(i.estado AS string) = :estado")
    Long countByEstado(@Param("estado") String estado);

    // RF-25: agrupar por tipo de documento
    @Query("SELECT i.tipoDocumento, COUNT(i) FROM InformacionDocumentada i GROUP BY i.tipoDocumento ORDER BY COUNT(i) DESC")
    List<Object[]> countGroupByTipoDocumento();

    // RF-26: agrupar por unidad
    @Query("SELECT i.unidad, COUNT(i) FROM InformacionDocumentada i GROUP BY i.unidad ORDER BY COUNT(i) DESC")
    List<Object[]> countGroupByUnidad();

    // RF-27: agrupar por año y mes (Oracle syntax con EXTRACT)
    @Query(value =
        "SELECT COALESCE(i.anio, EXTRACT(YEAR FROM i.fecha_solicitud)) AS anio, " +
        "       COALESCE(i.mes,  EXTRACT(MONTH FROM i.fecha_solicitud)) AS mes, " +
        "       COUNT(*) AS cantidad " +
        "FROM informacion_documentada i " +
        "WHERE COALESCE(i.anio, EXTRACT(YEAR FROM i.fecha_solicitud)) >= :anioDesde " +
        "  AND COALESCE(i.anio, EXTRACT(YEAR FROM i.fecha_solicitud)) <= :anioHasta " +
        "GROUP BY COALESCE(i.anio, EXTRACT(YEAR FROM i.fecha_solicitud)), " +
        "         COALESCE(i.mes,  EXTRACT(MONTH FROM i.fecha_solicitud)) " +
        "ORDER BY 1 ASC, 2 ASC",
        nativeQuery = true)
    List<Object[]> countGroupByAnioMes(@Param("anioDesde") Integer anioDesde, @Param("anioHasta") Integer anioHasta);
}
