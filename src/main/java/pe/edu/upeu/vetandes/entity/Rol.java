package pe.edu.upeu.vetandes.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity @Table(name="roles") @Getter @Setter @NoArgsConstructor
public class Rol {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,unique=true,length=40) private String nombre;
}
