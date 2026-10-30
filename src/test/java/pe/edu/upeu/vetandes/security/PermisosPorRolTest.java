package pe.edu.upeu.vetandes.security;
import org.junit.jupiter.api.Test;
import pe.edu.upeu.vetandes.entity.Rol;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
public class PermisosPorRolTest {
 private Rol rol(String nombre){Rol r=new Rol();r.setNombre(nombre);return r;}
 @Test void recepcionista_no_puede_anular_ni_ver_reportes(){
  var permisos=PermisosPorRol.permisos(Set.of(rol("RECEPCIONISTA")));
  assertTrue(permisos.contains("ATENCION_REGISTRAR"));assertTrue(permisos.contains("MASCOTA_ESCRIBIR"));
  assertFalse(permisos.contains("ATENCION_ANULAR"));assertFalse(permisos.contains("REPORTE_VER"));
 }
 @Test void veterinario_no_puede_registrar_mascotas_ni_atenciones(){
  var permisos=PermisosPorRol.permisos(Set.of(rol("VETERINARIO")));
  assertTrue(permisos.contains("ATENCION_ANULAR"));assertTrue(permisos.contains("REPORTE_VER"));
  assertFalse(permisos.contains("MASCOTA_ESCRIBIR"));assertFalse(permisos.contains("ATENCION_REGISTRAR"));
 }
 @Test void admin_todos_los_permisos_y_union_de_roles(){
  assertEquals(Permiso.values().length,PermisosPorRol.permisos(Set.of(rol("ADMIN"))).size());
  assertEquals(Permiso.values().length,PermisosPorRol.permisos(Set.of(rol("RECEPCIONISTA"),rol("VETERINARIO"))).size());
 }
}
