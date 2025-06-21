package kektor.auction.orchestrator.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ReservationDto(

        @NotNull
        @Positive
        Long userId,

        @NotNull
        @DecimalMin("0")
        BigDecimal amount,

        @Positive
        @NotNull
        Long sagaId

) {
}
