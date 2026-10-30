package pe.edu.upeu.vetandes.service.impl;
import pe.edu.upeu.vetandes.service.service.MascotaService;
import pe.edu.upeu.vetandes.dto.*;
import pe.edu.upeu.vetandes.entity.*;
import pe.edu.upeu.vetandes.repository.*;
import pe.edu.upeu.vetandes.mapper.VetAndesMapper;
import pe.edu.upeu.vetandes.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.*;
import java.util.*;
import java.math.*;
import java.time.*;

@Service @RequiredArgsConstructor
public class MascotaServiceImpl implements MascotaService {
 private static final Logger LOG=LoggerFactory.getLogger(MascotaServiceImpl.class);
 private final MascotaRepository mascotas;
 private final PropietarioRepository propietarios;
 private final AtencionRepository atenciones;
 @Transactional(readOnly=true)
 public List<MascotaResponseDTO> listar(Long propietarioId){
   if(propietarioId!=null && !propietarios.existsById(propietarioId))throw new RecursoNoEncontradoException("Propietario no encontrado: "+propietarioId);
   return mascotas.buscar(propietarioId).stream().map(VetAndesMapper::mascota).toList();
 }
 @Transactional(readOnly=true) public MascotaResponseDTO obtener(Long id){return VetAndesMapper.mascota(buscar(id));}
 @Transactional
 public MascotaResponseDTO crear(MascotaRequestDTO r){
   Propietario p=propietario(r.propietarioId());
   if(mascotas.existsByNombreIgnoreCaseAndPropietarioId(r.nombre().trim(),p.getId()))throw new ReglaNegocioException("El propietario ya tiene una mascota con ese nombre");
   Mascota m=new Mascota();establecer(m,r,p);Mascota guardada=mascotas.saveAndFlush(m);
   LOG.info("Mascota {} registrada",guardada.getId());return VetAndesMapper.mascota(guardada);
 }
 @Transactional
 public void editar(Long id,MascotaRequestDTO r){
   Mascota m=buscar(id);Propietario p=propietario(r.propietarioId());
   if(mascotas.existsByNombreIgnoreCaseAndPropietarioIdAndIdNot(r.nombre().trim(),p.getId(),id))throw new ReglaNegocioException("El propietario ya tiene una mascota con ese nombre");
   establecer(m,r,p);mascotas.saveAndFlush(m);LOG.info("Mascota {} editada",id);
 }
 @Transactional
 public void eliminar(Long id){
   Mascota m=buscar(id);
   if(atenciones.existsByMascotaId(id))throw new ReglaNegocioException("No se puede eliminar una mascota con atenciones registradas");
   mascotas.delete(m);mascotas.flush();LOG.info("Mascota {} eliminada",id);
 }
 public Mascota buscar(Long id){return mascotas.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Mascota no encontrada: "+id));}
 private Propietario propietario(Long id){return propietarios.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Propietario no encontrado: "+id));}
 private void establecer(Mascota m,MascotaRequestDTO r,Propietario p){m.setNombre(r.nombre().trim());m.setEspecie(r.especie());m.setRaza(r.raza()==null?null:r.raza().trim());m.setFechaNacimiento(r.fechaNacimiento());m.setSexo(r.sexo());m.setPropietario(p);}
}
