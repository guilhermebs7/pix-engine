package engine.pix_core.repository;

import engine.pix_core.BaseIntegrationTest;
import engine.pix_core.entity.Comprovante;
import engine.pix_core.entity.StatusTransacaoEnum;
import engine.pix_core.entity.Transacao;
import engine.pix_core.entity.Usuario;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class ComprovanteIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ComprovanteRepository comprovanteRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve salvar e buscar um comprovante do bando de dados Postgres real")
    void deveSalvarEBuscarComprovanteComTransicao(){
        //Arrange

        Usuario pagador= new Usuario();
        pagador.setNome("João Silva");
        pagador.setCpf("12345678901");
        pagador.setSaldo(new BigDecimal("1000.00"));
        pagador = usuarioRepository.save(pagador);

        Usuario recebedor= new Usuario();
        recebedor.setNome("Maria Souza");
        recebedor.setCpf("98765432100");
        recebedor.setSaldo(new BigDecimal("500.00"));
        recebedor = usuarioRepository.save(recebedor);

        Transacao transacao = new Transacao();
        transacao.setPagador(pagador);
        transacao.setRecebedor(recebedor);
        transacao.setValor(new BigDecimal("150.00"));
        transacao.setStatus(StatusTransacaoEnum.CONCLUIDA);
        transacao.setDataHora(LocalDate.now());
        transacao.setDescricao("Pagamento de Serviços");
        transacao= transacaoRepository.save(transacao);

        Comprovante comprovante= new Comprovante();
        comprovante.setTransacao(transacao);
        comprovante.setCodigoAutenticacao("AUTH-9988776655");
        comprovante.setDataEmissao(LocalDateTime.now());

        Comprovante comprovanteSalvo= comprovanteRepository.save(comprovante);

        assertThat(comprovanteSalvo.getId()).isNotNull();
        assertThat(comprovanteSalvo.getCodigoAutenticacao()).isEqualTo("AUTH-9988776655");

        var buscandoDoBanco= comprovanteRepository.findById(comprovanteSalvo.getId());
        assertThat(buscandoDoBanco).isPresent();
        assertThat(buscandoDoBanco.get().getTransacao().getValor()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(buscandoDoBanco.get().getTransacao().getPagador().getId()).isEqualTo(pagador.getId());
    }
}
