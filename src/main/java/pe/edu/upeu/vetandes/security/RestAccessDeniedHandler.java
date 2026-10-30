package pe.edu.upeu.vetandes.security;
import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
 @Override public void handle(HttpServletRequest req,HttpServletResponse res,AccessDeniedException e)throws IOException{
   RespuestaErrorSeguridad.escribir(req,res,HttpStatus.FORBIDDEN,"No tiene permisos para realizar esta operación");
 }
}
