package engine.pix_core.dto.Request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ComprovanteRequest(

        @NotNull
        Long TransacaoId

) {
}
