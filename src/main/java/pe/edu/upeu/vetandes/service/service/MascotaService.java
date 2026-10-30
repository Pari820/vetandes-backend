package pe.edu.upeu.vetandes.service.service;
import java.util.List;
import pe.edu.upeu.vetandes.dto.*;
public interface MascotaService {
 List<MascotaResponseDTO> listar(Long propietarioId);
 MascotaResponseDTO obtener(Long id);
 MascotaResponseDTO crear(MascotaRequestDTO r);
 void editar(Long id,MascotaRequestDTO r);
 void eliminar(Long id);
}
