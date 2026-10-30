package pe.edu.upeu.vetandes.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import pe.edu.upeu.vetandes.dto.*;
import pe.edu.upeu.vetandes.service.AuthService;
@RestController @RequestMapping("/api/v1/auth") @RequiredArgsConstructor
public class AuthController {
 private final AuthService service;
 @PostMapping("/login") public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO r){return service.login(r);}
 @GetMapping("/me") public PerfilResponseDTO perfil(Authentication auth){return service.perfil(auth.getName());}
}
