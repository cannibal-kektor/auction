package kektor.auction.sink.dto.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import kektor.auction.sink.model.BidStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record BidCDC(
        Long id,

        @JsonProperty("lot_id")
        Long lotId,

        @JsonProperty("bidder_id")
        Long bidderId,

        @JsonProperty("saga_id")
        Long sagaId,

        BigDecimal amount,

        @JsonProperty("creation_time")
        Instant creationTime,

        BidStatus status) {

}
