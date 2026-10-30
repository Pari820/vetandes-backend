package pe.edu.upeu.vetandes.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
import java.time.*;
import pe.edu.upeu.vetandes.entity.*;
import pe.edu.upeu.vetandes.dto.*;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
 Optional<Usuario> findByUsernameIgnoreCase(String username);
}
