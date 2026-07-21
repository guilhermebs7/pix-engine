package engine.pix_core.service;
import engine.pix_core.dto.Request.ChavePixRequest;
import engine.pix_core.dto.Request.TransacaoRequest;
import engine.pix_core.dto.Response.ChavePixResponse;
import engine.pix_core.entity.ChavePix;
import engine.pix_core.entity.TipoChave;
import engine.pix_core.entity.Usuario;
import engine.pix_core.repository.ChavePixRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChavePixServiceTest {

    @Mock
    private ChavePixRepository chavePixRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private ChavePixService chavePixService;

    private Usuario usuarioMock;

    @BeforeEach
    void setUp() {
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setNome("Guilherme");
    }

    @Test
    @DisplayName("Deve gerar um UUID automático quando o tipo da chave for ALEATORIA")
    void deveCadastrarChaveAleatoriaComSucesso() {
        ChavePixRequest request = new ChavePixRequest(1L, TipoChave.ALEATORIA, null);

        when(usuarioService.buscarEntidadeUsuario(1L)).thenReturn(usuarioMock);
        when(chavePixRepository.existsByValorChave(anyString())).thenReturn(false);

        when(chavePixRepository.save(any(ChavePix.class))).thenAnswer(invocation -> {
            ChavePix chaveParaSalvar = invocation.getArgument(0);
            chaveParaSalvar.setId(10L);
            return chaveParaSalvar;
        });


        ChavePixResponse response = chavePixService.cadastrarChave(request);


        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(10L, response.id());
        assertNotNull(response.valorChave());
        assertEquals("Guilherme", response.nomeTitular());
        assertEquals(TipoChave.ALEATORIA, response.tipoChave());

        verify(chavePixRepository, times(1)).save(any(ChavePix.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar chave duplicada")
    void deveLancarExcecaoQuandoChaveDuplicada() {
        ChavePixRequest request = new ChavePixRequest(1L, TipoChave.EMAIL, "teste@email.com");

        when(usuarioService.buscarEntidadeUsuario(1L)).thenReturn(usuarioMock);
        when(chavePixRepository.existsByValorChave(anyString())).thenReturn(true);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            chavePixService.cadastrarChave(request);
        });

        assertEquals("Esta chave PIX já esta cadastrada no sistema por outro usuário", exception.getMessage());
        verify(chavePixRepository, never()).save(any());
    }

}