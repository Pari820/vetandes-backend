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

@RestController @RequestMapping("/api/v1/propietarios") @RequiredArgsConstructor
public class PropietarioController {
 private final PropietarioService service;
 @GetMapping @PreAuthorize("hasAuthority('MASCOTA_LEER')")
 public List<PropietarioResponseDTO> listar(){return service.listar();}
}
