package pe.edu.upeu.vetandes.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
import java.time.*;
import pe.edu.upeu.vetandes.entity.*;
import pe.edu.upeu.vetandes.dto.*;

public interface PrestacionRepository extends JpaRepository<Prestacion,Long> {
 boolean existsByCodigoIgnoreCase(String codigo);
 boolean existsByCodigoIgnoreCaseAndIdNot(String codigo,Long id);
 @Query("select p from Prestacion p where (:especie is null or p.especieAplicable = :especie or p.especieAplicable = 'TODAS') order by p.codigo")
 List<Prestacion> buscar(@Param("especie") String especie);
}
