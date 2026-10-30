package pe.edu.upeu.vetandes.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
import java.time.*;
import pe.edu.upeu.vetandes.entity.*;
import pe.edu.upeu.vetandes.dto.*;

public interface MascotaRepository extends JpaRepository<Mascota,Long> {
 boolean existsByNombreIgnoreCaseAndPropietarioId(String nombre,Long propietarioId);
 boolean existsByNombreIgnoreCaseAndPropietarioIdAndIdNot(String nombre,Long propietarioId,Long id);
 @Query("select m from Mascota m join fetch m.propietario where (:propietarioId is null or m.propietario.id = :propietarioId) order by m.id")
 List<Mascota> buscar(@Param("propietarioId") Long propietarioId);
}
