package kektor.auction.bid.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record BidRequestDto(
        @NotNull
        @Positive
        Long lotId,

        @NotNull
        @PositiveOrZero
        Long lotVersion,

        @NotNull
        @Positive
        Long bidderId,

        @NotNull
        @Positive
        Long paymentAccountId,

        @NotNull
        @Positive
        BigDecimal amount
) {

}
