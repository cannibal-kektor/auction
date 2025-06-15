package kektor.auction.orchestrator.dto;

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
        Long itemVersion,

        @NotNull
        @Positive
        Long bidderId,

        @NotNull
        @Positive
        BigDecimal amount
) {

}
