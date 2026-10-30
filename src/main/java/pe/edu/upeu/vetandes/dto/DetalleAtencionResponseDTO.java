package pe.edu.upeu.vetandes.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import jakarta.validation.Valid;
public record DetalleAtencionResponseDTO(Long id,Long prestacionId,String codigo,String prestacionNombre,Integer cantidad,BigDecimal precioUnitario,BigDecimal subtotal) {}
