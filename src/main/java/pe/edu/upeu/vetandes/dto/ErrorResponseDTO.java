package pe.edu.upeu.vetandes.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import jakarta.validation.Valid;
public record ErrorResponseDTO(String timestamp,Integer status,String error,String mensaje,String ruta,Map<String,String> campos) {}
