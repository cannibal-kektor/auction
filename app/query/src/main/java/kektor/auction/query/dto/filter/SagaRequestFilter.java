package kektor.auction.query.dto.filter;

import jakarta.validation.constraints.Positive;
import kektor.auction.query.model.SagaStatus;

import java.time.Instant;
import java.util.Set;

public record SagaRequestFilter(

        Set<SagaStatus> sagaStatuses,

        @Positive
        Set<Long> lotsId,

        Instant creationTimeLowerLimit,

        Instant creationTimeUpperLimit,

        @Positive
        Set<Long> biddersId,

        Set<Long> paymentAccountIds,

        String problemDetail
) {
}
