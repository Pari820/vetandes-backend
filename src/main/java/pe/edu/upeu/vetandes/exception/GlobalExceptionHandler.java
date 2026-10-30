package pe.edu.upeu.vetandes.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;
@ControllerAdvice
public class GlobalExceptionHandler {
 private static final Logger LOG=LoggerFactory.getLogger(GlobalExceptionHandler.class);
 public static Map<String,Object> error(HttpStatus st,String mensaje,String ruta,Map<String,String> campos){
   Map<String,Object> m=new LinkedHashMap<>();m.put("timestamp",LocalDateTime.now().toString());m.put("status",st.value());
   m.put("error",st.getReasonPhrase());m.put("mensaje",mensaje);m.put("ruta",ruta);
   if(campos!=null && !campos.isEmpty())m.put("campos",campos);return m;
 }
 @ExceptionHandler(MethodArgumentNotValidException.class)
 ResponseEntity<?> validacion(MethodArgumentNotValidException e,HttpServletRequest r){
   Map<String,String> campos=new LinkedHashMap<>();e.getBindingResult().getFieldErrors().forEach(f->campos.putIfAbsent(f.getField(),f.getDefaultMessage()));
   LOG.warn("Validación rechazada: {}",campos);
   return ResponseEntity.badRequest().body(error(HttpStatus.BAD_REQUEST,"Datos inválidos",r.getRequestURI(),campos));
 }
 @ExceptionHandler({HttpMessageNotReadableException.class,MethodArgumentTypeMismatchException.class,ConstraintViolationException.class,IllegalArgumentException.class})
 ResponseEntity<?> datos(Exception e,HttpServletRequest r){
   LOG.warn("Petición inválida: {}",e.getMessage());return ResponseEntity.badRequest().body(error(HttpStatus.BAD_REQUEST,"Datos inválidos",r.getRequestURI(),null));
 }
 @ExceptionHandler(RecursoNoEncontradoException.class)
 ResponseEntity<?> noExiste(RecursoNoEncontradoException e,HttpServletRequest r){LOG.warn("404: {}",e.getMessage());return ResponseEntity.status(404).body(error(HttpStatus.NOT_FOUND,e.getMessage(),r.getRequestURI(),null));}
 @ExceptionHandler({ReglaNegocioException.class,DataIntegrityViolationException.class})
 ResponseEntity<?> conflicto(Exception e,HttpServletRequest r){LOG.warn("Regla rechazada: {}",e.getMessage());String msg=e instanceof ReglaNegocioException?e.getMessage():"Conflicto de datos únicos o referencias";return ResponseEntity.status(409).body(error(HttpStatus.CONFLICT,msg,r.getRequestURI(),null));}
 @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
 ResponseEntity<?> status(org.springframework.web.server.ResponseStatusException e,HttpServletRequest r){
   HttpStatus st=HttpStatus.valueOf(e.getStatusCode().value());return ResponseEntity.status(st).body(error(st,e.getReason()==null?st.getReasonPhrase():e.getReason(),r.getRequestURI(),null));
 }
 @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
 ResponseEntity<?> noPermiso(Exception e,HttpServletRequest r){return ResponseEntity.status(403).body(error(HttpStatus.FORBIDDEN,"No tiene permisos para realizar esta operación",r.getRequestURI(),null));}
 @ExceptionHandler(Exception.class)
 ResponseEntity<?> inesperado(Exception e,HttpServletRequest r){LOG.error("Error inesperado",e);return ResponseEntity.status(500).body(error(HttpStatus.INTERNAL_SERVER_ERROR,"Error interno del servidor",r.getRequestURI(),null));}
}
