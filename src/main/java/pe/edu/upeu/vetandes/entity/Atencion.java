package pe.edu.upeu.vetandes.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;
@Entity @Table(name="atencion") @Getter @Setter @NoArgsConstructor
public class Atencion {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false,length=9,unique=true) private String numero;
  @Column(nullable=false) private LocalDateTime fecha;
  @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="mascota_id",nullable=false) private Mascota mascota;
  @Column(nullable=false,precision=5,scale=2) private BigDecimal peso;
  @Column(length=300) private String diagnostico;
  @Column(nullable=false,length=12) private String estado;
  @Column(nullable=false,precision=10,scale=2) private BigDecimal subtotal;
  @Column(nullable=false,precision=10,scale=2) private BigDecimal descuento;
  @Column(nullable=false,precision=10,scale=2) private BigDecimal total;
  @Column(name="usuario_registro",nullable=false,length=30) private String usuarioRegistro;
  @Column(name="motivo_anulacion",length=200) private String motivoAnulacion;
}
