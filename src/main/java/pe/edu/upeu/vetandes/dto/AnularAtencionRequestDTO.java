package pe.edu.upeu.vetandes.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import jakarta.validation.Valid;
public record AnularAtencionRequestDTO(@NotBlank @Size(min=10,max=200) String motivo) {}
