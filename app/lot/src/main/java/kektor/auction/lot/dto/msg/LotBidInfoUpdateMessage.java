package kektor.auction.lot.dto.msg;

import java.math.BigDecimal;
import java.time.Instant;

public record LotBidInfoUpdateMessage(
        Instant timestamp,
        Long lotId,
        Long version,
        BigDecimal highestBid,
        Long winningBidId,
        boolean isRollback) implements LotUpdateMessage {
}
