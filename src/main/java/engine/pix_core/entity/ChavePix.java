package engine.pix_core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "chave_pix")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChavePix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_chave",nullable = false)
    private TipoChave tipoChave;

    @Column(nullable = false,unique = true)
    private String valorChave;

    @ManyToOne
    @JoinColumn(name = "usuario_id",nullable = false)
    private Usuario usuario;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;


}