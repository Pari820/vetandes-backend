package pe.edu.upeu.vetandes.service.impl;
import pe.edu.upeu.vetandes.service.service.PrestacionService;
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
public class PrestacionServiceImpl implements PrestacionService {
 private static final Logger LOG=LoggerFactory.getLogger(PrestacionServiceImpl.class);
 private final PrestacionRepository prestaciones;
 private final DetalleAtencionRepository detalles;
 @Transactional(readOnly=true)
 public List<PrestacionResponseDTO> listar(String especie){
   String esp=especie==null||especie.isBlank()?null:especie.trim().toUpperCase();
   if(esp!=null&&!List.of("PERRO","GATO","TODAS").contains(esp))throw new IllegalArgumentException("Especie inválida");
   return prestaciones.buscar(esp).stream().map(VetAndesMapper::prestacion).toList();
 }
 @Transactional(readOnly=true)
 public PrestacionResponseDTO obtener(Long id){return VetAndesMapper.prestacion(buscar(id));}
 @Transactional
 public PrestacionResponseDTO crear(PrestacionRequestDTO r){
   if(prestaciones.existsByCodigoIgnoreCase(r.codigo().trim()))throw new ReglaNegocioException("Ya existe una prestación con ese código");
   Prestacion p=new Prestacion();establecer(p,r);p.setActivo(true);
   Prestacion guardada=prestaciones.saveAndFlush(p);LOG.info("Prestación {} registrada",guardada.getCodigo());return VetAndesMapper.prestacion(guardada);
 }
 @Transactional
 public void editar(Long id,PrestacionRequestDTO r){
   Prestacion p=buscar(id);
   if(prestaciones.existsByCodigoIgnoreCaseAndIdNot(r.codigo().trim(),id))throw new ReglaNegocioException("Código de prestación duplicado");
   establecer(p,r);prestaciones.saveAndFlush(p);LOG.info("Prestación {} editada",id);
 }
 @Transactional
 public void eliminar(Long id){
   Prestacion p=buscar(id);
   if(detalles.existsByPrestacionId(id))throw new ReglaNegocioException("La prestación tiene atenciones: use PATCH /prestaciones/{id}/estado para darla de baja");
   prestaciones.delete(p);prestaciones.flush();LOG.info("Prestación {} eliminada",id);
 }
 @Transactional
 public PrestacionResponseDTO cambiarEstado(Long id,EstadoRequestDTO r){
   Prestacion p=buscar(id);p.setActivo(r.activo());prestaciones.save(p);LOG.info("Prestación {} cambió a activo={}",id,r.activo());return VetAndesMapper.prestacion(p);
 }
 public Prestacion buscar(Long id){return prestaciones.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Prestación no encontrada: "+id));}
 private void establecer(Prestacion p,PrestacionRequestDTO r){p.setCodigo(r.codigo().trim());p.setNombre(r.nombre().trim());p.setPrecio(r.precio().setScale(2,RoundingMode.HALF_UP));p.setEspecieAplicable(r.especieAplicable());}
}
