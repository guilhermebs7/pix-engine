package engine.pix_core.service;
import engine.pix_core.dto.Request.TransacaoRequest;
import engine.pix_core.dto.Response.TransacaoResponse;
import engine.pix_core.entity.ChavePix;
import engine.pix_core.entity.StatusTransacaoEnum;
import engine.pix_core.entity.Usuario;
import engine.pix_core.repository.ChavePixRepository;
import engine.pix_core.repository.ComprovanteRepository;
import engine.pix_core.repository.TransacaoRepository;
import engine.pix_core.repository.UsuarioRepository;
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
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ChavePixRepository chavePixRepository;

    @Mock
    private ComprovanteService comprovanteService;

    @InjectMocks
    private TransacaoService transacaoService;

    private Usuario pagador;
    private Usuario recebedor;
    private ChavePix chaveDestino;

    @BeforeEach
    void setUp(){
        pagador= new Usuario();
        pagador.setId(1L);
        pagador.setNome("Guilherme Barbosa");
        pagador.setSaldo(new BigDecimal("100.00"));


        recebedor= new Usuario();
        recebedor.setId(2L);
        recebedor.setNome("Maria souza");
        recebedor.setSaldo(BigDecimal.ZERO);

        chaveDestino= new ChavePix();
        chaveDestino.setUsuario(recebedor);
        chaveDestino.setValorChave("maria@email.com");
    }
    @Test
    @DisplayName("Deve realizar transferencia PIX xom sucesso quando houver saldo")
    void processarTransferenciaSucesso(){
        TransacaoRequest request= new TransacaoRequest(1L,"maria@email.com",new BigDecimal("40.00"),"Almoço");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(pagador));
        when(chavePixRepository.findByValorChave("maria@email.com")).thenReturn(Optional.of(chaveDestino));
        when(transacaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TransacaoResponse response= transacaoService.processarTransferencia(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("60.00"),pagador.getSaldo());
        assertEquals(new BigDecimal("40.00"),recebedor.getSaldo());
        assertEquals(StatusTransacaoEnum.CONCLUIDA,response.status());

        verify(usuarioRepository,times(2)).save(any());
        verify(comprovanteService,times(1)).gerarComprovante(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o pagador não tiver saldo suficiente")
    void processarTransferenciaSaldoInsuficiente(){
        TransacaoRequest request= new TransacaoRequest(1L,"maria@email.com",new BigDecimal("200.00"),"Celular Novo");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(pagador));
        when(chavePixRepository.findByValorChave("maria@email.com")).thenReturn(Optional.of(chaveDestino));

        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class,()->{
            transacaoService.processarTransferencia(request);
        });

        assertEquals("Saldo insuficiente para realizar a transação",exception.getMessage());
        verify(transacaoRepository,never()).save(any());
    }


}