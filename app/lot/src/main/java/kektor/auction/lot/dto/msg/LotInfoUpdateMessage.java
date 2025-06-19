package kektor.auction.lot.dto.msg;

import kektor.auction.lot.dto.LotDto;

public record LotInfoUpdateMessage(LotDto lot) implements LotUpdateMessage {
    @Override
    public Long lotId() {
        return lot.id();
    }
}
