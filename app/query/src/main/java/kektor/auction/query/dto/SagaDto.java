package kektor.auction.query.dto;


import kektor.auction.query.model.SagaStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record SagaDto(

        Long sagaId,

        SagaStatus status,

        Long lotId,

        Instant creationTime,

        Long bidderId,

        Long paymentAccountId,

        BigDecimal newBidAmount,

        BigDecimal compensateBidAmount,

        Long compensateWinningBidId,

        Long compensateWinnerId,

        String problemDetail

) {
}