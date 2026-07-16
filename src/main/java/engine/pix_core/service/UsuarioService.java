package engine.pix_core.service;

import engine.pix_core.dto.Request.UsuarioRequest;
import engine.pix_core.dto.Response.UsuarioReponse;
import engine.pix_core.entity.Usuario;
import engine.pix_core.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioReponse cadastrarUsuario(UsuarioRequest request){
        if(usuarioRepository.findByCpf(request.cpf()).isPresent()){
            throw  new IllegalArgumentException("Já existe um usuário cadastrado com este CPF.");
        }

        Usuario usuario= new Usuario();
        usuario.setNome(request.nome());
        usuario.setCpf(request.cpf());
        usuario.setSaldo(BigDecimal.ZERO);

        Usuario usuarioSalvo= usuarioRepository.save(usuario);

        return new UsuarioReponse(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getCpf(),
                usuarioSalvo.getSaldo(),
                new ArrayList<>()
        );
    }

    public Usuario buscarEntidadeUsuario(Long id){
        return usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário com ID "+id+" não encontrado"));

    }



}
