package engine.pix_core.controller;

import engine.pix_core.dto.Request.UsuarioRequest;
import engine.pix_core.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean                                                // usado para n "sujar" o bd local durante os testes
    private UsuarioService usuarioService;


    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso via API")
    void deveCadastrarUsuarioComSucesso() throws  Exception{
        UsuarioRequest requestInvalido= new UsuarioRequest("Guilherme Barbosa","12345678900");

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request se os dados do usuário forem inválidos")
    void deveRetornarErroValidacao() throws Exception {
        // Envia dados vazios para forçar o erro das anotações de validação (@NotBlank/@NotNull)
        UsuarioRequest requestInvalido = new UsuarioRequest("", "");

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }



}