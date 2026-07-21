package engine.pix_core.controller;
import engine.pix_core.dto.Response.ComprovanteResponse;
import engine.pix_core.service.ComprovanteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(ComprovanteController.class)
class ComprovanteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComprovanteService comprovanteService;

    @Test
    @DisplayName("Deve retornar o comprovante com status 200 OK quando o ID for válido")
    void deveBuscarComprovantePorId() throws Exception {

        Long comprovanteId = 10L;
        ComprovanteResponse mockResponse = new ComprovanteResponse(
                comprovanteId,
                "AUTH-998877",
                LocalDateTime.now(),
                new BigDecimal("150.00"),
                "João Silva",
                "12345678901",
                "Maria Souza",
                "Pagamento de Serviços"
        );

        when(comprovanteService.buscarComprovantePorId(comprovanteId)).thenReturn(mockResponse);


        mockMvc.perform(get("/comprovantes/{id}", comprovanteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comprovanteId").value(10))
                .andExpect(jsonPath("$.codigoAutenticacao").value("AUTH-998877"))
                .andExpect(jsonPath("$.pagadorNome").value("João Silva"))
                .andExpect(jsonPath("$.valor").value(150.00));
    }
}

