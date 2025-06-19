package kektor.auction.sse.dto;

public record LotCategoriesUpdateMessage(CategoryEvent source, Long lotId) implements LotUpdateMessage {
}
