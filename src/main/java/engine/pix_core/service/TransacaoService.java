package engine.pix_core.service;
import engine.pix_core.dto.Request.TransacaoRequest;
import engine.pix_core.dto.Response.TransacaoResponse;
import engine.pix_core.entity.*;
import engine.pix_core.repository.ChavePixRepository;
import engine.pix_core.repository.TransacaoRepository;
import engine.pix_core.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ChavePixRepository chavePixRepository;

    @Autowired
    private ComprovanteService comprovanteService;

    @Transactional
    public TransacaoResponse processarTransferencia(TransacaoRequest request){

        Usuario pagador= usuarioRepository.findById(request.pagadorId())
                .orElseThrow(()-> new IllegalArgumentException("Usuário pagador não encontrado"));

        ChavePix chaveDestino= chavePixRepository.findByValorChave(request.ChaveDestino())
                .orElseThrow(()-> new IllegalArgumentException("Chave PIX de destino não encontrada."));

        Usuario recebedor= chaveDestino.getUsuario();

        if(pagador.getId().equals(recebedor.getId())){
            throw new IllegalArgumentException("Não é possível realizar uma transferência PIX para você mesmo");


        }

        if (pagador.getSaldo().compareTo(request.valor()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a transação");

        }
        // executa a transferência física dos valores
        pagador.setSaldo(pagador.getSaldo().subtract(request.valor()));
        recebedor.setSaldo(recebedor.getSaldo().add(request.valor()));

        //salva os novos saldos no banco de dados
        usuarioRepository.save(pagador);
        usuarioRepository.save(recebedor);

        Transacao transacao= new Transacao();
        transacao.setPagador(pagador);
        transacao.setRecebedor(recebedor);
        transacao.setValor(request.valor());
        transacao.setStatus(StatusTransacaoEnum.CONCLUIDA);
        transacao.setDataHora(LocalDate.now());
        transacao.setDescricao(request.descricao());

        Transacao transacaoSalva= transacaoRepository.save(transacao);

        Comprovante comprovante= comprovanteService.gerarComprovante(transacaoSalva);

        return new TransacaoResponse(
                transacaoSalva.getId(),
                pagador.getNome(),
                recebedor.getNome(),
                transacaoSalva.getValor(),
                transacaoSalva.getStatus(),
                transacaoSalva.getDataHora(),
                transacaoSalva.getDescricao()
        );
    }


}
