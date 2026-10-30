package pe.edu.upeu.vetandes.service;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pe.edu.upeu.vetandes.service.impl.AtencionServiceImpl;
import pe.edu.upeu.vetandes.entity.*;
import pe.edu.upeu.vetandes.dto.*;
import pe.edu.upeu.vetandes.repository.*;
import pe.edu.upeu.vetandes.exception.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
public class AtencionServiceTest {
 private final AtencionRepository atenciones=mock(AtencionRepository.class);
 private final MascotaRepository mascotas=mock(MascotaRepository.class);
 private final PrestacionRepository prestaciones=mock(PrestacionRepository.class);
 private final DetalleAtencionRepository detalles=mock(DetalleAtencionRepository.class);
 private final AtencionServiceImpl servicio=new AtencionServiceImpl(atenciones,mascotas,prestaciones,detalles);
 private Mascota perro(){
   var p=new Propietario();p.setId(1L);p.setNombres("Rosa Pérez");
   var m=new Mascota();m.setId(1L);m.setNombre("Luna");m.setEspecie("PERRO");m.setPropietario(p);m.setFechaNacimiento(LocalDate.of(2022,4,15));return m;
 }
 private Prestacion prestacion(long id,String especie,String precio){
   var p=new Prestacion();p.setId(id);p.setCodigo("PRS-%03d".formatted(id));p.setNombre("Prestación "+id);p.setEspecieAplicable(especie);p.setPrecio(new BigDecimal(precio));p.setActivo(true);return p;
 }
 @Test void tercer_detalle_incompatible_rechaza_409_y_no_guarda_cabecera(){
   when(mascotas.findById(1L)).thenReturn(Optional.of(perro()));
   when(prestaciones.findById(1L)).thenReturn(Optional.of(prestacion(1,"TODAS","60.00")));
   when(prestaciones.findById(2L)).thenReturn(Optional.of(prestacion(2,"TODAS","75.00")));
   when(prestaciones.findById(3L)).thenReturn(Optional.of(prestacion(3,"GATO","90.00")));
   var r=new AtencionRequestDTO(1L,new BigDecimal("22.50"),"Control",List.of(
       new DetalleAtencionRequestDTO(1L,1),new DetalleAtencionRequestDTO(2L,1),new DetalleAtencionRequestDTO(3L,1)));
   assertThrows(ReglaNegocioException.class,()->servicio.registrar(r,"recepcion"));
   verify(atenciones,never()).saveAndFlush(any());verify(detalles,never()).save(any());
 }
 @Test void registra_tres_prestaciones_y_calcula_descuento_en_backend(){
   when(mascotas.findById(1L)).thenReturn(Optional.of(perro()));
   when(prestaciones.findById(1L)).thenReturn(Optional.of(prestacion(1,"TODAS","60.00")));
   when(prestaciones.findById(2L)).thenReturn(Optional.of(prestacion(2,"TODAS","75.00")));
   when(prestaciones.findById(5L)).thenReturn(Optional.of(prestacion(5,"TODAS","40.00")));
   when(atenciones.siguienteNumero()).thenReturn(5L);
   when(atenciones.saveAndFlush(any())).thenAnswer(i->{Atencion a=i.getArgument(0);a.setId(5L);return a;});
   when(detalles.save(any())).thenAnswer(i->i.getArgument(0));
   var r=new AtencionRequestDTO(1L,new BigDecimal("22.50"),"Control",List.of(
       new DetalleAtencionRequestDTO(1L,1),new DetalleAtencionRequestDTO(2L,1),new DetalleAtencionRequestDTO(5L,1)));
   AtencionResponseDTO respuesta=servicio.registrar(r,"recepcion");
   assertEquals("AT-000005",respuesta.numero());
   assertEquals(new BigDecimal("175.00"),respuesta.subtotal());
   assertEquals(new BigDecimal("17.50"),respuesta.descuento());
   assertEquals(new BigDecimal("157.50"),respuesta.total());
   assertEquals(3,respuesta.detalles().size());
 }
}
