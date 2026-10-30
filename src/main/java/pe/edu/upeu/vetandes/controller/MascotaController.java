package pe.edu.upeu.vetandes.controller;
import pe.edu.upeu.vetandes.dto.*;
import pe.edu.upeu.vetandes.service.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.net.URI;
import java.util.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@RestController @RequestMapping("/api/v1/mascotas") @RequiredArgsConstructor
public class MascotaController {
 private final MascotaService service;
 @GetMapping @PreAuthorize("hasAuthority('MASCOTA_LEER')")
 public List<MascotaResponseDTO> listar(@RequestParam(required=false) Long propietarioId){return service.listar(propietarioId);}
 @GetMapping("/{id}") @PreAuthorize("hasAuthority('MASCOTA_LEER')")
 public MascotaResponseDTO obtener(@PathVariable Long id){return service.obtener(id);}
 @PostMapping @PreAuthorize("hasAuthority('MASCOTA_ESCRIBIR')")
 public ResponseEntity<MascotaResponseDTO> crear(@Valid @RequestBody MascotaRequestDTO r){
   var m=service.crear(r);return ResponseEntity.created(URI.create("/api/v1/mascotas/"+m.id())).body(m);
 }
 @PutMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @PreAuthorize("hasAuthority('MASCOTA_ESCRIBIR')")
 public void editar(@PathVariable Long id,@Valid @RequestBody MascotaRequestDTO r){service.editar(id,r);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @PreAuthorize("hasAuthority('MASCOTA_ESCRIBIR')")
 public void eliminar(@PathVariable Long id){service.eliminar(id);}
}
