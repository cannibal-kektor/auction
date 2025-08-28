package kektor.auction.query.dto.filter;

import jakarta.validation.constraints.Positive;
import kektor.auction.query.model.BidStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record BidRequestFilter(

        @Positive
        Set<Long> lotsId,

        @Positive
        Set<Long> biddersId,

        @Positive
        Set<Long> paymentAccountsId,

        @Positive
        BigDecimal amountLowerLimit,

        @Positive
        BigDecimal amountUpperLimit,

        Instant creationTimeLowerLimit,

        Instant creationTimeUpperLimit,

        Set<BidStatus> statuses
) {
}
