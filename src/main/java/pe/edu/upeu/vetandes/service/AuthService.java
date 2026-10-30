package pe.edu.upeu.vetandes.service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import pe.edu.upeu.vetandes.dto.*;
import pe.edu.upeu.vetandes.repository.UsuarioRepository;
import pe.edu.upeu.vetandes.security.*;
import pe.edu.upeu.vetandes.config.JwtService;
import java.util.*;
@Service @RequiredArgsConstructor
public class AuthService {
 private final UsuarioRepository repo;
 private final UsuarioDetailsService usuarioDetails;
 private final JwtService jwt;
 private final PasswordEncoder encoder;
 @Transactional(readOnly=true)
 public LoginResponseDTO login(LoginRequestDTO request){
   var u=repo.findByUsernameIgnoreCase(request.username().trim()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuario o contraseña incorrectos"));
   if(!Boolean.TRUE.equals(u.getActivo())||!encoder.matches(request.password(),u.getPassword()))
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuario o contraseña incorrectos");
   UserDetails ud=usuarioDetails.loadUserByUsername(u.getUsername());
   return new LoginResponseDTO(jwt.emitir(ud),"Bearer",jwt.getExpiraSegundos(),u.getUsername(),u.getNombreCompleto(),
     u.getRoles().stream().map(r->r.getNombre()).sorted().toList(),PermisosPorRol.permisos(u.getRoles()));
 }
 @Transactional(readOnly=true)
 public PerfilResponseDTO perfil(String username){
   var u=repo.findByUsernameIgnoreCase(username).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Sesión no disponible"));
   return new PerfilResponseDTO(u.getUsername(),u.getNombreCompleto(),u.getRoles().stream().map(r->r.getNombre()).sorted().toList(),PermisosPorRol.permisos(u.getRoles()));
 }
}
