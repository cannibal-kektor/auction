package kektor.auction.lot.dto.msg;

import kektor.auction.lot.dto.CategoryDto;

public record CategoryEventMessage(EventType eventType, CategoryDto categoryDto) {

    public enum EventType {
        CREATED,
        UPDATED,
        DELETED
    }

}
