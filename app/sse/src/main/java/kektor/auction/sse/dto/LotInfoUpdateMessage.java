package kektor.auction.sse.dto;


public record LotInfoUpdateMessage(LotDto lot) implements LotUpdateMessage {
    @Override
    public Long lotId() {
        return lot.id();
    }
}
