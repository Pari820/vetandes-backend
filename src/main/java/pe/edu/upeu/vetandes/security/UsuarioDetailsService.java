package pe.edu.upeu.vetandes.security;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.vetandes.repository.UsuarioRepository;
@Service @RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {
 private final UsuarioRepository usuarios;
 @Override @Transactional(readOnly=true)
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
   var u=usuarios.findByUsernameIgnoreCase(username.trim()).orElseThrow(()->new UsernameNotFoundException("Credenciales incorrectas"));
   return User.withUsername(u.getUsername()).password(u.getPassword())
     .authorities(PermisosPorRol.authorities(u.getRoles())).disabled(!Boolean.TRUE.equals(u.getActivo())).build();
 }
}
