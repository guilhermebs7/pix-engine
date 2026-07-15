package engine.pix_core.dto.Response;

import engine.pix_core.entity.StatusTransacaoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponse(
        Long id,
        String pagadorNome,
        String recebedorNome,
        BigDecimal valor,
        StatusTransacaoEnum status,
        LocalDateTime dataHora,
        String mensagemSucesso
) {
}
