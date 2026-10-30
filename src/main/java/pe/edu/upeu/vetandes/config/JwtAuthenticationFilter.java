package pe.edu.upeu.vetandes.config;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
 private final JwtService jwt;
 private final UserDetailsService usuarios;
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain) throws ServletException,IOException {
  String bearer=req.getHeader("Authorization");
  if(bearer!=null && bearer.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication()==null){
   try{
    String username=jwt.username(bearer.substring(7));
    var u=usuarios.loadUserByUsername(username);
    if(u.isEnabled() && u.isAccountNonLocked() && u.isAccountNonExpired() && u.isCredentialsNonExpired()){
      var auth=new UsernamePasswordAuthenticationToken(u,null,u.getAuthorities());
      auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
   }catch(RuntimeException e){SecurityContextHolder.clearContext();req.setAttribute("jwt_error","Token inválido o vencido");}
  }
  chain.doFilter(req,res);
 }
}
