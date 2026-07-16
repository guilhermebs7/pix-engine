package engine.pix_core.service;

import engine.pix_core.dto.Response.ComprovanteResponse;
import engine.pix_core.entity.Comprovante;
import engine.pix_core.entity.Transacao;
import engine.pix_core.repository.ComprovanteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ComprovanteService {

    @Autowired
    private ComprovanteRepository comprovanteRepository;

    @Transactional
    public Comprovante gerarComprovante(Transacao transacao){
        Comprovante comprovante= new Comprovante();
        comprovante.setTransacao(transacao);
        comprovante.setDataEmissao(LocalDateTime.now());

        String codigoAutenticacao= UUID.randomUUID().toString().substring(0,18).toUpperCase();
        comprovante.setCodigoAutenticacao(codigoAutenticacao);

        return comprovanteRepository.save(comprovante);
    }

    public ComprovanteResponse buscarComprovantePorId(Long id){
        Comprovante comprovante= comprovanteRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Comprovante não encontrado com o ID: "+ id));


        Transacao transacao= comprovante.getTransacao();

        return new ComprovanteResponse(
                comprovante.getId(),
                comprovante.getCodigoAutenticacao(),
                comprovante.getDataEmissao(),
                transacao.getValor(),
                transacao.getPagador().getNome(),
                transacao.getPagador().getCpf(),
                transacao.getRecebedor().getNome(),
                transacao.getDescricao());


    }

}
