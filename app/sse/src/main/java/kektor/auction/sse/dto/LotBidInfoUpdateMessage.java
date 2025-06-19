package kektor.auction.sse.dto;


import java.math.BigDecimal;

public record LotBidInfoUpdateMessage(Long lotId, Long version, BigDecimal highestBid,
                                      Long winningBidId, boolean isRollback) implements LotUpdateMessage {
}
