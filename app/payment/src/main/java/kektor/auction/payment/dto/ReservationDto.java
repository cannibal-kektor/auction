package kektor.auction.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import kektor.auction.payment.model.CreditOperation;

import java.math.BigDecimal;
import java.time.Instant;

public record ReservationDto(

        @NotNull
        @Positive
        Long userId,

        @NotNull
        @DecimalMin("0")
        BigDecimal amount,

        @NotNull
        @Past
        Instant timestamp,

        @Positive
        @NotNull
        Long bidId,

        @Positive
        @NotNull
        Long sagaId,

        @NotNull
        @Positive
        Long version

) {
}
