package pe.edu.upeu.vetandes.mapper;
import pe.edu.upeu.vetandes.dto.*;
import pe.edu.upeu.vetandes.entity.*;
import java.util.*;

public final class VetAndesMapper {
 private VetAndesMapper(){}
 public static PropietarioResponseDTO propietario(Propietario p){
   return new PropietarioResponseDTO(p.getId(),p.getDni(),p.getNombres(),p.getTelefono());
 }
 public static PrestacionResponseDTO prestacion(Prestacion p){
   return new PrestacionResponseDTO(p.getId(),p.getCodigo(),p.getNombre(),p.getPrecio(),p.getEspecieAplicable(),p.getActivo());
 }
 public static MascotaResponseDTO mascota(Mascota m){
   return new MascotaResponseDTO(m.getId(),m.getNombre(),m.getEspecie(),m.getRaza(),m.getFechaNacimiento(),m.getSexo(),m.getPropietario().getId(),m.getPropietario().getNombres());
 }
 public static DetalleAtencionResponseDTO detalle(DetalleAtencion d){
   return new DetalleAtencionResponseDTO(d.getId(),d.getPrestacion().getId(),d.getPrestacion().getCodigo(),d.getPrestacion().getNombre(),d.getCantidad(),d.getPrecioUnitario(),d.getSubtotal());
 }
 public static AtencionResponseDTO atencion(Atencion a,List<DetalleAtencion> ds){
   return new AtencionResponseDTO(a.getId(),a.getNumero(),a.getFecha(),a.getMascota().getId(),a.getMascota().getNombre(),a.getMascota().getEspecie(),a.getMascota().getPropietario().getNombres(),a.getPeso(),a.getDiagnostico(),a.getEstado(),a.getSubtotal(),a.getDescuento(),a.getTotal(),a.getUsuarioRegistro(),a.getMotivoAnulacion(),ds.stream().map(VetAndesMapper::detalle).toList());
 }
}
