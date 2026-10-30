package pe.edu.upeu.vetandes.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import pe.edu.upeu.vetandes.exception.GlobalExceptionHandler;
import java.io.IOException;
public final class RespuestaErrorSeguridad {
 private static final ObjectMapper JSON=new ObjectMapper();
 private RespuestaErrorSeguridad(){}
 public static void escribir(HttpServletRequest req,HttpServletResponse res,HttpStatus status,String mensaje)throws IOException{
   res.setStatus(status.value());res.setContentType("application/json;charset=UTF-8");
   JSON.writeValue(res.getWriter(),GlobalExceptionHandler.error(status,mensaje,req.getRequestURI(),null));
 }
}
