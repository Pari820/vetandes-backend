package pe.edu.upeu.vetandes.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
import java.time.*;
import pe.edu.upeu.vetandes.entity.*;
import pe.edu.upeu.vetandes.dto.*;

public interface AtencionRepository extends JpaRepository<Atencion,Long> {
 boolean existsByMascotaId(Long mascotaId);
 @Query(value="SELECT atencion_numero_seq.NEXTVAL FROM dual",nativeQuery=true)
 Long siguienteNumero();
 @Query("select a from Atencion a where (:mascota is null or upper(a.mascota.nombre) like concat('%',concat(upper(:mascota),'%'))) " +
        "and (:especie is null or a.mascota.especie = :especie) " +
        "and (:estado is null or a.estado = :estado) " +
        "and (:desde is null or a.fecha >= :desde) and (:hasta is null or a.fecha < :hasta) " +
        "order by a.fecha desc,a.id desc")
 List<Atencion> buscar(@Param("mascota") String mascota,@Param("especie") String especie,@Param("estado") String estado,
 @Param("desde") LocalDateTime desde,@Param("hasta") LocalDateTime hasta);
 @Query("select new pe.edu.upeu.vetandes.dto.ReporteEspecieDTO(a.mascota.especie,count(a.id),sum(a.total)) " +
        "from Atencion a where a.estado = 'REGISTRADA' and (:desde is null or a.fecha >= :desde) " +
        "and (:hasta is null or a.fecha < :hasta) group by a.mascota.especie order by sum(a.total) desc")
 List<ReporteEspecieDTO> reporte(@Param("desde") LocalDateTime desde,@Param("hasta") LocalDateTime hasta);
}
