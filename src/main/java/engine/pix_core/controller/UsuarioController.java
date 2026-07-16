package engine.pix_core.controller;

import engine.pix_core.dto.Request.UsuarioRequest;
import engine.pix_core.dto.Response.UsuarioReponse;
import engine.pix_core.entity.Usuario;
import engine.pix_core.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioReponse> cadastrar(@RequestBody @Valid UsuarioRequest request){
        UsuarioReponse response= usuarioService.cadastrarUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioReponse> buscarPorId(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarEntidadeUsuario(id);

        List<String> chavesTexto = usuario.getChavepix().stream()
                .map(chave -> chave.getValorChave())
                .toList();

        UsuarioReponse response = new UsuarioReponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getSaldo(),
                chavesTexto
        );

        return ResponseEntity.ok(response);
    }
}
