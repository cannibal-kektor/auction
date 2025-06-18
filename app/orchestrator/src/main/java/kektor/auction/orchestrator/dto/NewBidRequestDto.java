package kektor.auction.orchestrator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record NewBidRequestDto(

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
        BigDecimal amount
) {

}
