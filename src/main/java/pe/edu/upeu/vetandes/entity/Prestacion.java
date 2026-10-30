package pe.edu.upeu.vetandes.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
@Entity @Table(name="prestacion") @Getter @Setter @NoArgsConstructor
public class Prestacion {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,unique=true,length=7) private String codigo;
  @Column(nullable=false,length=80) private String nombre;
  @Column(nullable=false,precision=10,scale=2) private BigDecimal precio;
  @Column(name="especie_aplicable",nullable=false,length=6) private String especieAplicable;
  @Convert(converter=org.hibernate.type.NumericBooleanConverter.class)
  @Column(nullable=false) private Boolean activo=true;
}
