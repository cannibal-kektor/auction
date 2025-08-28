package kektor.auction.query.dto;

import kektor.auction.query.model.LotStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record LotDto(

        Long id,

        Long version,

        String name,

        String description,

        LotStatus status,

        Long sellerId,

        Long sellerPaymentAccountId,

        BigDecimal initialPrice,

        Instant auctionStart,

        Instant auctionEnd,

        BigDecimal highestBid,

        Long winnerId,

        Long winningBidId,

        Long bidsCount,

        Set<CategoryDto> categories

) {


}
