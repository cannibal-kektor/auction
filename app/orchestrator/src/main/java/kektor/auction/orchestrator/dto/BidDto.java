package kektor.auction.orchestrator.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record BidDto(

        Long lotId,

        Long bidderId,

        Long sagaId,

        Instant createdOn,

        BigDecimal amount
) {
}

