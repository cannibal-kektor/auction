package kektor.auction.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

public record ReplenishmentDto(

        @NotNull
        @Positive
        Long userId,

        @NotNull
        @DecimalMin("0")
        BigDecimal amount,

        @NotNull
        @Past
        Instant timestamp,

        @NotNull
        @Positive
        Long version
) {
}
