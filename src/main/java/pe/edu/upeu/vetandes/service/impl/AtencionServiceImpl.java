package pe.edu.upeu.vetandes.service.impl;
import pe.edu.upeu.vetandes.service.AtencionCalculos;
import pe.edu.upeu.vetandes.service.service.AtencionService;
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
public class AtencionServiceImpl implements AtencionService {
 private static final Logger LOG=LoggerFactory.getLogger(AtencionServiceImpl.class);
 private final AtencionRepository atenciones;
 private final MascotaRepository mascotas;
 private final PrestacionRepository prestaciones;
 private final DetalleAtencionRepository detalles;
 @Transactional
 public AtencionResponseDTO registrar(AtencionRequestDTO r,String username){
   Mascota mascota=mascotas.findById(r.mascotaId()).orElseThrow(()->new RecursoNoEncontradoException("Mascota no encontrada: "+r.mascotaId()));
   if(r.detalles()==null||r.detalles().isEmpty()||r.detalles().size()>8)throw new IllegalArgumentException("La atención debe tener de 1 a 8 detalles");
   if(r.peso()==null||r.peso().compareTo(BigDecimal.ZERO)<=0)throw new IllegalArgumentException("El peso debe ser mayor que cero");
   Set<Long> ids=new HashSet<>();List<DetalleAtencion> items=new ArrayList<>();BigDecimal subtotal=BigDecimal.ZERO;
   for(DetalleAtencionRequestDTO x:r.detalles()){
     if(x.prestacionId()==null||x.cantidad()==null||x.cantidad()<1||x.cantidad()>3)throw new IllegalArgumentException("Cantidad de 1 a 3 y prestación obligatoria");
     if(!ids.add(x.prestacionId()))throw new IllegalArgumentException("No se puede repetir una prestación");
     Prestacion p=prestaciones.findById(x.prestacionId()).orElseThrow(()->new RecursoNoEncontradoException("Prestación no encontrada: "+x.prestacionId()));
     if(!Boolean.TRUE.equals(p.getActivo()))throw new ReglaNegocioException("La prestación "+p.getCodigo()+" está inactiva");
     if(!"TODAS".equals(p.getEspecieAplicable())&&!p.getEspecieAplicable().equals(mascota.getEspecie()))
       throw new ReglaNegocioException("La prestación "+p.getCodigo()+" no aplica a la especie "+mascota.getEspecie());
     DetalleAtencion d=new DetalleAtencion();d.setPrestacion(p);d.setCantidad(x.cantidad());d.setPrecioUnitario(p.getPrecio());
     d.setSubtotal(p.getPrecio().multiply(BigDecimal.valueOf(x.cantidad())).setScale(2,RoundingMode.HALF_UP));
     subtotal=subtotal.add(d.getSubtotal());items.add(d);
   }
   subtotal=subtotal.setScale(2,RoundingMode.HALF_UP);
   BigDecimal descuento=AtencionCalculos.calcularDescuento(subtotal,ids.size());
   Long sec=atenciones.siguienteNumero();
   Atencion a=new Atencion();a.setNumero("AT-%06d".formatted(sec));a.setFecha(LocalDateTime.now());a.setMascota(mascota);
   a.setPeso(r.peso().setScale(2,RoundingMode.HALF_UP));a.setDiagnostico(r.diagnostico());a.setEstado("REGISTRADA");a.setSubtotal(subtotal);
   a.setDescuento(descuento);a.setTotal(subtotal.subtract(descuento));a.setUsuarioRegistro(username);
   Atencion guardada=atenciones.saveAndFlush(a);
   for(DetalleAtencion x:items){x.setAtencion(guardada);detalles.save(x);}
   detalles.flush();LOG.info("Atención {} registrada; {} detalles, descuento={}, total={}",guardada.getNumero(),items.size(),descuento,guardada.getTotal());
   return VetAndesMapper.atencion(guardada,items);
 }
 @Transactional(readOnly=true)
 public AtencionResponseDTO obtener(Long id){Atencion a=buscar(id);return VetAndesMapper.atencion(a,detalles.findAllByAtencionIdOrderByIdAsc(id));}
 @Transactional(readOnly=true)
 public List<AtencionResponseDTO> listar(String mascota,String especie,String estado,LocalDate desde,LocalDate hasta){
   fechas(desde,hasta);
   String esp=normalizar(especie);String est=normalizar(estado);
   if(esp!=null&&!List.of("PERRO","GATO","OTRO").contains(esp))throw new IllegalArgumentException("Especie inválida");
   if(est!=null&&!List.of("REGISTRADA","ANULADA").contains(est))throw new IllegalArgumentException("Estado inválido");
   String nombre=mascota==null||mascota.isBlank()?null:mascota.trim();
   return atenciones.buscar(nombre,esp,est,inicio(desde),fin(hasta)).stream()
     .map(a->VetAndesMapper.atencion(a,detalles.findAllByAtencionIdOrderByIdAsc(a.getId()))).toList();
 }
 @Transactional
 public AtencionResponseDTO anular(Long id,AnularAtencionRequestDTO r){
   Atencion a=buscar(id);
   if(!"REGISTRADA".equals(a.getEstado()))throw new ReglaNegocioException("Solo se anulan atenciones REGISTRADAS");
   if(a.getFecha().isBefore(LocalDateTime.now().minusHours(48)))throw new ReglaNegocioException("Han transcurrido más de 48 horas desde el registro");
   a.setEstado("ANULADA");a.setMotivoAnulacion(r.motivo().trim());atenciones.save(a);LOG.info("Atención {} anulada",a.getNumero());
   return VetAndesMapper.atencion(a,detalles.findAllByAtencionIdOrderByIdAsc(id));
 }
 @Transactional(readOnly=true)
 public List<ReporteEspecieDTO> reporte(LocalDate desde,LocalDate hasta){fechas(desde,hasta);return atenciones.reporte(inicio(desde),fin(hasta));}
 private Atencion buscar(Long id){return atenciones.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Atención no encontrada: "+id));}
 private static String normalizar(String s){return s==null||s.isBlank()?null:s.trim().toUpperCase();}
 private static LocalDateTime inicio(LocalDate d){return d==null?null:d.atStartOfDay();}
 private static LocalDateTime fin(LocalDate d){return d==null?null:d.plusDays(1).atStartOfDay();}
 private static void fechas(LocalDate desde,LocalDate hasta){if(desde!=null&&hasta!=null&&desde.isAfter(hasta))throw new IllegalArgumentException("La fecha desde no puede ser posterior a hasta");}
}
