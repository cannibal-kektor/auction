package kektor.auction.lot.dto.msg;

public record LotCategoriesUpdateMessage(CategoryEventMessage source, Long lotId) implements UpdateMessage {
}
