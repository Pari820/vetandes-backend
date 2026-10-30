package pe.edu.upeu.vetandes.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import jakarta.validation.Valid;
public record PrestacionRequestDTO(@NotBlank @Pattern(regexp="PRS-\\d{3}",message="Debe tener el formato PRS-000") String codigo,@NotBlank @Size(min=3,max=80) String nombre,@NotNull @DecimalMin(value="0.0",inclusive=false) @Digits(integer=8,fraction=2) BigDecimal precio,@NotBlank @Pattern(regexp="PERRO|GATO|TODAS") String especieAplicable) {}
