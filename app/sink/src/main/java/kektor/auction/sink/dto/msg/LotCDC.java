package kektor.auction.sink.dto.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import kektor.auction.sink.model.LotStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record LotCDC(

        Long id,

        Long version,

        String name,

        LotStatus status,

        String description,

        @JsonProperty("seller_id")
        Long sellerId,

        @JsonProperty("initial_price")
        BigDecimal initialPrice,

        @JsonProperty("auction_start")
        Instant auctionStart,

        @JsonProperty("auction_end")
        Instant auctionEnd,

        @JsonProperty("highest_bid")
        BigDecimal highestBid,

        @JsonProperty("winning_bid_id")
        Long winningBidId,

        @JsonProperty("bids_count")
        Long bidsCount

) {


}
