package pe.edu.upeu.vetandes.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import jakarta.validation.Valid;
public record LoginResponseDTO(String token,String tipo,Long expiraEnSegundos,String username,String nombreCompleto,List<String> roles,List<String> permisos) {}
