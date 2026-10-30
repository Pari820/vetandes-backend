package pe.edu.upeu.vetandes.service;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
public class AtencionCalculosTest {
 @Test void tres_prestaciones_aplican_diez_por_ciento(){
  assertEquals(new BigDecimal("17.50"),AtencionCalculos.calcularDescuento(new BigDecimal("175.00"),3));
 }
 @Test void una_o_dos_no_aplican_descuento(){
  assertEquals(new BigDecimal("0.00"),AtencionCalculos.calcularDescuento(new BigDecimal("175.00"),2));
 }
 @Test void redondeo_monetario_correcto(){
  assertEquals(new BigDecimal("3.34"),AtencionCalculos.calcularDescuento(new BigDecimal("33.35"),3));
 }
}
