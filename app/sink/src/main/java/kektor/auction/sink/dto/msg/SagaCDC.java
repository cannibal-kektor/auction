package kektor.auction.sink.dto.msg;


import com.fasterxml.jackson.annotation.JsonProperty;
import kektor.auction.sink.model.SagaStatus;
import org.springframework.http.ProblemDetail;

import java.math.BigDecimal;
import java.time.Instant;

public record SagaCDC(

        @JsonProperty("saga_id")
        Long sagaId,

        SagaStatus status,

        @JsonProperty("lot_id")
        Long lotId,

        @JsonProperty("creation_time")
        Instant creationTime,

        @JsonProperty("bidder_id")
        Long bidderId,

        @JsonProperty("payment_account_id")
        Long paymentAccountId,

        @JsonProperty("new_bid_amount")
        BigDecimal newBidAmount,

        @JsonProperty("compensate_bid_amount")
        BigDecimal compensateBidAmount,

        @JsonProperty("compensate_winning_bid_id")
        Long compensateWinningBidId,

        @JsonProperty("compensate_winner_id")
        Long compensateWinnerId,

        @JsonProperty("problem_detail")
        ProblemDetail problemDetail

) {
}