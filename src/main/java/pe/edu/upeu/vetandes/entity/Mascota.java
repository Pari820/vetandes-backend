package pe.edu.upeu.vetandes.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
@Entity @Table(name="mascota",uniqueConstraints=@UniqueConstraint(name="uq_mascota",columnNames={"nombre","propietario_id"})) @Getter @Setter @NoArgsConstructor
public class Mascota {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,length=40) private String nombre;
  @Column(nullable=false,length=6) private String especie;
  @Column(length=40) private String raza;
  @Column(name="fecha_nacimiento",nullable=false) private LocalDate fechaNacimiento;
  @JdbcTypeCode(SqlTypes.CHAR) @Column(nullable=false,length=1,columnDefinition="CHAR(1)") private String sexo;
  @ManyToOne(fetch=FetchType.LAZY,optional=false)
  @JoinColumn(name="propietario_id",nullable=false) private Propietario propietario;
}
