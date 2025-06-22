package kektor.auction.sse.dto;


import java.time.Instant;

public record LotStatusUpdateMessage(
        Instant timestamp,
        Long lotId,
        LotStatus status) implements LotUpdateMessage {
}
