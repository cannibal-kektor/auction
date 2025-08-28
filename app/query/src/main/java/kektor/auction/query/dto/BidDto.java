package kektor.auction.query.dto;

import kektor.auction.query.model.BidStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record BidDto(

        Long id,

        Long lotId,

        Long bidderId,

        Long paymentAccountId,

        Long sagaId,

        BigDecimal amount,

        Instant creationTime,

        BidStatus status) {

}
