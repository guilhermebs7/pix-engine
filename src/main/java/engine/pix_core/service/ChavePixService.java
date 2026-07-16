package engine.pix_core.service;

import engine.pix_core.dto.Request.ChavePixRequest;
import engine.pix_core.dto.Response.ChavePixResponse;
import engine.pix_core.entity.ChavePix;
import engine.pix_core.entity.TipoChave;
import engine.pix_core.entity.Usuario;
import engine.pix_core.repository.ChavePixRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ChavePixService {

    @Autowired
    private ChavePixRepository chavePixRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public ChavePixResponse cadastrarChave(ChavePixRequest request){
        Usuario usuario= usuarioService.buscarEntidadeUsuario(request.usuarioId());

        String valorFinalChave= request.valorChave();
        if(request.tipoChave() == TipoChave.ALEATORIA){
            valorFinalChave= UUID.randomUUID().toString();
        }

        if(chavePixRepository.existsByValorChave(valorFinalChave)){
            throw new IllegalArgumentException("Esta chave PIX já esta cadastrada no sistema por outro usuário");

        }

        ChavePix chavePix= new ChavePix();
        chavePix.setTipoChave(request.tipoChave());
        chavePix.setValorChave(valorFinalChave);
        chavePix.setUsuario(usuario);
        chavePix.setDataCriacao(LocalDateTime.now());

        ChavePix chaveSalva= chavePixRepository.save(chavePix);

        return new ChavePixResponse(
                chaveSalva.getId(),
                chaveSalva.getTipoChave(),
                chaveSalva.getValorChave(),
                usuario.getNome(),
                chaveSalva.getDataCriacao()
        );

    }

}
