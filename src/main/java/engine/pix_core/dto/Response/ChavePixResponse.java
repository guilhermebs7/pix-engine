package engine.pix_core.dto.Response;

import engine.pix_core.entity.TipoChave;

import java.time.LocalDateTime;

public record ChavePixResponse(
        Long id,
        TipoChave tipoChave,
        String valorChave,
        String nomeTitular,
        LocalDateTime dataCriacao
) {
}
