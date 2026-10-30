package pe.edu.upeu.vetandes.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import jakarta.validation.Valid;
public record MascotaResponseDTO(Long id,String nombre,String especie,String raza,LocalDate fechaNacimiento,String sexo,Long propietarioId,String propietarioNombre) {}
