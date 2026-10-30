package pe.edu.upeu.vetandes.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import jakarta.validation.Valid;
public record DetalleAtencionRequestDTO(@NotNull Long prestacionId,@NotNull @Min(1) @Max(3) Integer cantidad) {}
