package kektor.auction.sink.dto.msg;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryCDC(

        Long id,

        @JsonProperty("parent_id")
        Long parentId,

        String name
) {
}
