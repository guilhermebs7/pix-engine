package engine.pix_core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comprovante")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comprovante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Transacao transacao;

    @Column(nullable = false,unique = true)
    private String codigoAutenticacao;

    @Column(nullable = false)
    private LocalDateTime dataEmissao;
}
