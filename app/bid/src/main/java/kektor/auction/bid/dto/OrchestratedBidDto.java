package kektor.auction.bid.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record OrchestratedBidDto(

        Long lotId,

        Long bidderId,

        Long sagaId,

        Instant createdOn,

        BigDecimal amount,

        Long lotVersion

) {
}
