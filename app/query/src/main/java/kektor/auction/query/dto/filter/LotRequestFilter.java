package kektor.auction.query.dto.filter;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import kektor.auction.query.model.LotStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public record LotRequestFilter(

        @Size(min = 2, max = 255)
        String name,

        @Size(max = 4000)
        String description,

        Set<LotStatus> statuses,

        Set<Long> sellersId,

        @PositiveOrZero
        BigDecimal initialPriceLowerLimit,

        @PositiveOrZero
        BigDecimal initialPriceUpperLimit,

        Instant auctionStartLowerLimit,

        Instant auctionStartUpperLimit,

        Instant auctionEndLowerLimit,

        Instant auctionEndUpperLimit,

        @PositiveOrZero
        BigDecimal highestBidLowerLimit,

        @PositiveOrZero
        BigDecimal highestBidUpperLimit,

        @PositiveOrZero
        Long bidsCountLowerLimit,

        @PositiveOrZero
        Long bidsCountUpperLimit,

        Set<Long> winnersIds,

        List<Long> categoriesId

) {
}
