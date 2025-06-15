package kektor.auction.orchestrator.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record LotDto(

        Long id,

        Long version,

        String name,

        Long sellerId,

        BigDecimal initialPrice,

        Instant auctionStart,

        Instant auctionEnd,

        BigDecimal highestBid,

        Long bidsCount

) {


}
