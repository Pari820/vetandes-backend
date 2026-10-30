package pe.edu.upeu.vetandes.security;
import pe.edu.upeu.vetandes.entity.Rol;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.*;
public final class PermisosPorRol {
 private PermisosPorRol(){}
 private static final Map<String,Set<Permiso>> MAP=Map.of(
  "ADMIN",EnumSet.allOf(Permiso.class),
  "RECEPCIONISTA",EnumSet.of(Permiso.PRESTACION_LEER,Permiso.MASCOTA_LEER,Permiso.MASCOTA_ESCRIBIR,Permiso.ATENCION_LEER,Permiso.ATENCION_REGISTRAR),
  "VETERINARIO",EnumSet.of(Permiso.PRESTACION_LEER,Permiso.PRESTACION_ESCRIBIR,Permiso.MASCOTA_LEER,Permiso.ATENCION_LEER,Permiso.ATENCION_ANULAR,Permiso.REPORTE_VER));
 public static List<String> permisos(Collection<Rol> roles){
   return roles.stream().flatMap(r->MAP.getOrDefault(r.getNombre(),Set.of()).stream()).map(Enum::name).distinct().sorted().toList();
 }
 public static List<GrantedAuthority> authorities(Collection<Rol> roles){
   List<GrantedAuthority> all=new ArrayList<>();
   roles.forEach(r->all.add(new SimpleGrantedAuthority("ROLE_"+r.getNombre())));
   permisos(roles).forEach(p->all.add(new SimpleGrantedAuthority(p)));
   return all;
 }
}
