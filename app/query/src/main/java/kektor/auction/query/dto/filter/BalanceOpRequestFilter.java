package kektor.auction.query.dto.filter;

import jakarta.validation.constraints.Positive;
import kektor.auction.query.model.CreditStatus;
import kektor.auction.query.model.OperationType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record BalanceOpRequestFilter(

        Set<Long> accountIds,

        @Positive
        BigDecimal amountLowerLimit,

        @Positive
        BigDecimal amountUpperLimit,

        Instant createdAtLowerLimit,

        Instant createdAtUpperLimit,

        Set<OperationType> operationTypes,

        Set<CreditStatus> creditStatuses,

        @Positive
        Set<Long> bidsId

) {
}
