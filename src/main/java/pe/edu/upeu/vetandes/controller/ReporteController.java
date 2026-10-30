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

@RestController @RequestMapping("/api/v1/reportes") @RequiredArgsConstructor
public class ReporteController {
 private final AtencionService service;
 @GetMapping("/atenciones-por-especie") @PreAuthorize("hasAuthority('REPORTE_VER')")
 public List<ReporteEspecieDTO> reporte(@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate desde,
 @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate hasta){return service.reporte(desde,hasta);}
}
