package kektor.auction.lot.dto.msg;

import kektor.auction.lot.dto.CategoryDto;

public record CategoryEventMessage(EVENT_TYPE eventType, CategoryDto categoryDto) {

    public enum EVENT_TYPE {
        CREATED,
        UPDATED,
        DELETED
    }

}
