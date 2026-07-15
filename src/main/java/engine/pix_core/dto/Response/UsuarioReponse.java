package engine.pix_core.dto.Response;

import java.math.BigDecimal;
import java.util.List;

public record UsuarioReponse(
        Long id,
        String nome,
        String cpf,
        BigDecimal valor,
        List<String> chavesPix

) {
}
