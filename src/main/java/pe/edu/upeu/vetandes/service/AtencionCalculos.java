package pe.edu.upeu.vetandes.service;
import java.math.*;
public final class AtencionCalculos {
 private AtencionCalculos(){}
 public static BigDecimal calcularDescuento(BigDecimal subtotal,int prestacionesDistintas){
   return prestacionesDistintas>=3?subtotal.multiply(new BigDecimal("0.10")).setScale(2,RoundingMode.HALF_UP):new BigDecimal("0.00");
 }
}
