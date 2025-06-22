package kektor.auction.lot.dto.msg;

import kektor.auction.lot.dto.LotDto;

import java.time.Instant;

public record LotInfoUpdateMessage(
        Instant timestamp,
        LotDto lot) implements LotUpdateMessage {

    @Override
    public Long lotId() {
        return lot.id();
    }
}
