package pe.edu.upeu.vetandes.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import jakarta.validation.Valid;
public record PrestacionResponseDTO(Long id,String codigo,String nombre,BigDecimal precio,String especieAplicable,Boolean activo) {}
