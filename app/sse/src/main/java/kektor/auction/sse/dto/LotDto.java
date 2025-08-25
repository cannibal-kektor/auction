package kektor.auction.sse.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record LotDto(

        Long id,

        Long version,

        String name,

        LotStatus status,

        String description,

        Long sellerId,

        Long sellerPaymentAccountId,

        BigDecimal initialPrice,

        Instant auctionStart,

        Instant auctionEnd,

        Set<CategoryDto> categories,

        BigDecimal highestBid,

        Long winnerId,

        Long winningBidId,

        Long bidsCount

) {


}
