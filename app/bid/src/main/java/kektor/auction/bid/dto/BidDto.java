package kektor.auction.bid.dto;

import kektor.auction.bid.model.BidStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record BidDto(

        Long id,

        Long lotId,

        Long bidderId,

        BigDecimal amount,

        Instant createdOn,

        BidStatus status

) implements Comparable<BidDto> {

    @Override
    public int compareTo(BidDto another) {
        return another.amount().compareTo(this.amount());
    }

}
