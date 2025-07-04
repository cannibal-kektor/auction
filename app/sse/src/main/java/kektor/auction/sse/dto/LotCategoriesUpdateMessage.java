package kektor.auction.sse.dto;

import java.time.Instant;

public record LotCategoriesUpdateMessage(
        Instant timestamp,
        Long lotId,
        CategoryEventMessage source) implements LotUpdateMessage {
}
