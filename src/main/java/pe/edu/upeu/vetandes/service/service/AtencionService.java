package pe.edu.upeu.vetandes.service.service;
import java.util.List;
import java.time.LocalDate;
import pe.edu.upeu.vetandes.dto.*;
public interface AtencionService {
 AtencionResponseDTO registrar(AtencionRequestDTO r,String username);
 AtencionResponseDTO obtener(Long id);
 List<AtencionResponseDTO> listar(String mascota,String especie,String estado,LocalDate desde,LocalDate hasta);
 AtencionResponseDTO anular(Long id,AnularAtencionRequestDTO r);
 List<ReporteEspecieDTO> reporte(LocalDate desde,LocalDate hasta);
}
