package kektor.auction.bid.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record BidCreateDto(

        Long lotId,

        Long bidderId,

        Long sagaId,

        BigDecimal amount,

        Instant creationTime

) {
}
