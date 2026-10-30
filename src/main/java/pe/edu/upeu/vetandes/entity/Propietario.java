package pe.edu.upeu.vetandes.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity @Table(name="propietario") @Getter @Setter @NoArgsConstructor
public class Propietario {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,length=8,unique=true) private String dni;
  @Column(nullable=false,length=120) private String nombres;
  @Column(length=15) private String telefono;
}
