package kektor.auction.orchestrator.dto;


import java.math.BigDecimal;
import java.time.Instant;

public record BidDto(

        Long id,

        Long lotId,

        Long bidderId,

        BigDecimal amount,

        Instant createdOn,

        BidStatus status

) {

}
