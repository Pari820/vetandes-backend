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

@RestController @RequestMapping("/api/v1/prestaciones") @RequiredArgsConstructor
public class PrestacionController {
 private final PrestacionService service;
 @GetMapping @PreAuthorize("hasAuthority('PRESTACION_LEER')")
 public List<PrestacionResponseDTO> listar(@RequestParam(required=false) String especie){return service.listar(especie);}
 @GetMapping("/{id}") @PreAuthorize("hasAuthority('PRESTACION_LEER')")
 public PrestacionResponseDTO obtener(@PathVariable Long id){return service.obtener(id);}
 @PostMapping @PreAuthorize("hasAuthority('PRESTACION_ESCRIBIR')")
 public ResponseEntity<PrestacionResponseDTO> crear(@Valid @RequestBody PrestacionRequestDTO r){
   var p=service.crear(r);return ResponseEntity.created(URI.create("/api/v1/prestaciones/"+p.id())).body(p);
 }
 @PutMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @PreAuthorize("hasAuthority('PRESTACION_ESCRIBIR')")
 public void editar(@PathVariable Long id,@Valid @RequestBody PrestacionRequestDTO r){service.editar(id,r);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @PreAuthorize("hasAuthority('PRESTACION_ESCRIBIR')")
 public void eliminar(@PathVariable Long id){service.eliminar(id);}
 @PatchMapping("/{id}/estado") @PreAuthorize("hasAuthority('PRESTACION_ESCRIBIR')")
 public PrestacionResponseDTO estado(@PathVariable Long id,@Valid @RequestBody EstadoRequestDTO r){return service.cambiarEstado(id,r);}
}
