package kektor.auction.orchestrator.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record BidCreateDto(

        Long lotId,

        Long bidderId,

        Long sagaId,

        Long lotVersion,

        BigDecimal amount,

        Instant createdOn

) {
}

