package kektor.auction.sse.dto;


import java.time.Instant;

public record LotInfoUpdateMessage(
        Instant timestamp,
        LotDto lot) implements LotUpdateMessage {

    @Override
    public Long lotId() {
        return lot.id();
    }
}
