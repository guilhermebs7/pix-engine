package engine.pix_core.dto.Request;

import engine.pix_core.entity.TipoChave;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChavePixRequest(
        @NotNull
        Long usuarioId,

        @NotNull
        TipoChave tipoChave,

        @NotBlank
        String valorChave
) {
}
