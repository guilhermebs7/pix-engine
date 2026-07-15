package engine.pix_core.dto.Request;

import engine.pix_core.entity.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransacaoRequest(

        @NotBlank
        String  pagadorId,

        @NotBlank
        String ChaveDestino,

        @NotNull
        BigDecimal valor

) {
}
