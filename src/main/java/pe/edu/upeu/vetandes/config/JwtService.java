package pe.edu.upeu.vetandes.config;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
@Service
public class JwtService {
 private final SecretKey clave;
 private final long expiraSegundos;
 public JwtService(@Value("${app.jwt.secret}") String secreto,@Value("${app.jwt.expiracion-minutos:60}") long minutos){
  this.clave=Keys.hmacShaKeyFor(Decoders.BASE64.decode(secreto));this.expiraSegundos=minutos*60;
 }
 public String emitir(UserDetails d){Instant hoy=Instant.now();return Jwts.builder().subject(d.getUsername()).issuedAt(Date.from(hoy))
   .expiration(Date.from(hoy.plusSeconds(expiraSegundos))).signWith(clave).compact();}
 public String username(String token){return Jwts.parser().verifyWith(clave).build().parseSignedClaims(token).getPayload().getSubject();}
 public long getExpiraSegundos(){return expiraSegundos;}
}
