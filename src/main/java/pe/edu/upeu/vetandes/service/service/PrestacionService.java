package pe.edu.upeu.vetandes.service.service;
import java.util.List;
import pe.edu.upeu.vetandes.dto.*;
public interface PrestacionService {
 List<PrestacionResponseDTO> listar(String especie);
 PrestacionResponseDTO obtener(Long id);
 PrestacionResponseDTO crear(PrestacionRequestDTO r);
 void editar(Long id,PrestacionRequestDTO r);
 void eliminar(Long id);
 PrestacionResponseDTO cambiarEstado(Long id,EstadoRequestDTO r);
}
