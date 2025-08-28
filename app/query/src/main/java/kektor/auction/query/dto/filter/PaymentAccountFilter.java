package kektor.auction.query.dto.filter;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record PaymentAccountFilter(

        Set<Long> usersId,

        BigDecimal balanceLowerLimit,

        @PositiveOrZero
        BigDecimal balanceUpperLimit,

        @Past
        Instant registrationDateLowerLimit,

        Instant registrationDateUpperLimit

//        TODO - Implement following
//        Set<BidStatus> bidStatuses,
//
//        @PositiveOrZero
//        Long totalBidsLowerLimit,
//
//        @PositiveOrZero
//        Long totalBidsUpperLimit,
//
//        @PositiveOrZero
//        BigDecimal highestBidLowerLimit,
//
//        @PositiveOrZero
//        BigDecimal highestBidUpperLimit,
//
//        Set<LotStatus> lotStatuses,
//
//        @PositiveOrZero
//        Long totalLotsLowerLimit,
//
//        @PositiveOrZero
//        Long totalLotsUpperLimit

) {
}
