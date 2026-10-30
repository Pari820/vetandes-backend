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

@RestController @RequestMapping("/api/v1/atenciones") @RequiredArgsConstructor
public class AtencionController {
 private final AtencionService service;
 @PostMapping @PreAuthorize("hasAuthority('ATENCION_REGISTRAR')")
 public ResponseEntity<AtencionResponseDTO> registrar(@Valid @RequestBody AtencionRequestDTO r,org.springframework.security.core.Authentication auth){
   var a=service.registrar(r,auth.getName());return ResponseEntity.created(URI.create("/api/v1/atenciones/"+a.id())).body(a);
 }
 @GetMapping @PreAuthorize("hasAuthority('ATENCION_LEER')")
 public List<AtencionResponseDTO> listar(@RequestParam(required=false) String mascota,@RequestParam(required=false) String especie,
 @RequestParam(required=false) String estado,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate desde,
 @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate hasta){return service.listar(mascota,especie,estado,desde,hasta);}
 @GetMapping("/{id}") @PreAuthorize("hasAuthority('ATENCION_LEER')")
 public AtencionResponseDTO obtener(@PathVariable Long id){return service.obtener(id);}
 @PatchMapping("/{id}/anular") @PreAuthorize("hasAuthority('ATENCION_ANULAR')")
 public AtencionResponseDTO anular(@PathVariable Long id,@Valid @RequestBody AnularAtencionRequestDTO r){return service.anular(id,r);}
}
