package pe.edu.upeu.vetandes.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.*;
@Entity @Table(name="usuarios") @Getter @Setter @NoArgsConstructor
public class Usuario {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,unique=true,length=30) private String username;
  @Column(nullable=false,length=100) private String password;
  @Column(name="nombrecompleto",nullable=false,length=120) private String nombreCompleto;
  @Convert(converter=org.hibernate.type.NumericBooleanConverter.class)
  @Column(nullable=false) private Boolean activo=true;
  @ManyToMany(fetch=FetchType.EAGER)
  @JoinTable(name="usuario_roles",joinColumns=@JoinColumn(name="usuario_id"),inverseJoinColumns=@JoinColumn(name="rol_id"))
  private Set<Rol> roles=new HashSet<>();
}
