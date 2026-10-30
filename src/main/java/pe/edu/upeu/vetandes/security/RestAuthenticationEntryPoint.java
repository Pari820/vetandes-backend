package pe.edu.upeu.vetandes.security;
import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
 @Override public void commence(HttpServletRequest req,HttpServletResponse res,AuthenticationException e)throws IOException{
   res.setHeader("WWW-Authenticate","Bearer");
   Object causa=req.getAttribute("jwt_error");
   RespuestaErrorSeguridad.escribir(req,res,HttpStatus.UNAUTHORIZED,causa==null?"Debe iniciar sesión":"Token inválido o vencido");
 }
}
