package kektor.auction.lot.dto.msg;

import kektor.auction.lot.model.Lot;

import java.time.Instant;

public record LotStatusUpdateMessage(
        Instant timestamp,
        Long lotId,
        Lot.Status status) implements LotUpdateMessage {
}