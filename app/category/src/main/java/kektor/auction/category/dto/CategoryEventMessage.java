package kektor.auction.category.dto;

public record CategoryEventMessage(EventType eventType, CategoryDto categoryDto) {

    public enum EventType {
        CREATED,
        UPDATED,
        DELETED
    }

}
