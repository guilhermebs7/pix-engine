package engine.pix_core.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ComprovanteResponse(
        Long comprovanteId,
        String codigoAutenticacao,
        LocalDateTime dataEmissao,
        BigDecimal valor,
        String pagadorNome,
        String pagadorCpf,
        String recebedorNome,
        String descricaoTransacao
) {
}
