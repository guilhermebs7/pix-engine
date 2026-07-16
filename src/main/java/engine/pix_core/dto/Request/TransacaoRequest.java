package engine.pix_core.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransacaoRequest(

        @NotNull
        Long pagadorId,

        @NotBlank
        String ChaveDestino,

        @NotNull
        BigDecimal valor,
        @NotBlank
        String descricao

) {
}
