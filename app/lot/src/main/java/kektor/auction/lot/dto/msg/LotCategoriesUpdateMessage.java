package kektor.auction.lot.dto.msg;

import java.time.Instant;

public record LotCategoriesUpdateMessage(
        Instant timestamp,
        Long lotId,
        CategoryEventMessage source) implements LotUpdateMessage {
}
