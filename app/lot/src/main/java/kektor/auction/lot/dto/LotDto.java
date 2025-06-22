package kektor.auction.lot.dto;

import kektor.auction.lot.model.Lot;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record LotDto(

        Long id,

        Long version,

        String name,

        Lot.Status status,

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
