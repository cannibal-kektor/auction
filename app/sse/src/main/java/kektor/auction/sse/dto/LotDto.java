package kektor.auction.sse.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record LotDto(

        Long id,

        Long version,

        String name,

        String description,

        Long sellerId,

        BigDecimal initialPrice,

        Instant auctionStart,

        Instant auctionEnd,

        Set<CategoryDto> categories,

        BigDecimal highestBid,

        Long winningBidId,

        Long bidsCount

) {


}
