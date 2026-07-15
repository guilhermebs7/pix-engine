package engine.pix_core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pagador_id",nullable = false)
    private Usuario pagador;

    @ManyToOne
    @JoinColumn(name = "recebedor_id",nullable = false)
    private Usuario recebedor;

    @Column(nullable = false )
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTransacaoEnum status;

    @Column(name = "data_hora",nullable = false)
    private LocalDate dataHora;

    @Column(nullable = false)
    private String descricao;
}
