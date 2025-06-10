package kektor.auction.lot.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

//@Value
//@NonFinal
//@Builder
//@EqualsAndHashCode(cacheStrategy = LAZY)
public record LotFetchDto(

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

        Long bidsCount

) {


}
