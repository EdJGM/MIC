package ec.edu.espe.inventario.repository;

import ec.edu.espe.inventario.model.entity.ObjetivoEspecifico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObjetivoEspecificoRepository extends JpaRepository<ObjetivoEspecifico, Long> {

    boolean existsByNombre(String nombre);

    Optional<ObjetivoEspecifico> findByNombre(String nombre);
}
