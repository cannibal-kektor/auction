package kektor.auction.lot.dto;

public record CategoryEventMessage(EVENT_TYPE eventType, CategoryDto categoryDto) {

    public enum EVENT_TYPE {
        CREATED,
        UPDATED,
        DELETED
    }

}
