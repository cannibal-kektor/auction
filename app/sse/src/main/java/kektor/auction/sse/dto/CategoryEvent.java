package kektor.auction.sse.dto;


public record CategoryEvent(EVENT_TYPE eventType, CategoryDto categoryDto) {

    public enum EVENT_TYPE {
        CREATED,
        UPDATED,
        DELETED
    }

}
