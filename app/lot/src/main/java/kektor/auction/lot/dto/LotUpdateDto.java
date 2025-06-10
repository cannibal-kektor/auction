package kektor.auction.lot.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import kektor.auction.lot.validation.AuctionDatesValid;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Builder
@Jacksonized
@AuctionDatesValid
public record LotUpdateDto(

        @NotNull
        @Positive
        Long id,

        @NotNull
        @PositiveOrZero
        Long version,

        @NotBlank
        @Size(min = 2, max = 255)
        String name,

        @NotBlank
        @Size(min = 10, max = 4000)
        String description,

        @NotNull
        @DecimalMin(value = "0")
        BigDecimal initialPrice,

        @NotNull
        @Future(message = "{Item.auctionStart.Future}")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        Instant auctionStart,

        @NotNull
        @Future(message = "{Item.auctionEnd.Future}")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        Instant auctionEnd,

        @Valid
        Set<@Positive Long> categoriesId
) {
}
