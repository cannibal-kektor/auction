package kektor.auction.orchestrator.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record LotDto(

        Long id,

        Long version,

        String name,

        LotStatus status,

        Long sellerId,

        BigDecimal initialPrice,

        Instant auctionStart,

        Instant auctionEnd,

        BigDecimal highestBid,

        Long winningBidId,

        Long bidsCount

) {


}
