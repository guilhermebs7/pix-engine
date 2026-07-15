package engine.pix_core.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UsuarioRequest(
        @NotBlank
        @Size(min = 3,max = 100)
        String nome,

        @NotBlank
        String cpf


) {
}
