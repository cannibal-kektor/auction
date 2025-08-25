package kektor.auction.sse.dto;


import java.math.BigDecimal;
import java.time.Instant;

public record LotBidInfoUpdateMessage(
        Instant timestamp,
        Long lotId,
        Long version,
        BigDecimal highestBid,
        Long winnerId,
        Long winningBidId,
        boolean isRollback) implements LotUpdateMessage {
}
