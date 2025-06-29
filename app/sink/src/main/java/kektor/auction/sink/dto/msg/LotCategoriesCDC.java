package kektor.auction.sink.dto.msg;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LotCategoriesCDC(

        @JsonProperty("lot_id")
        Long lotId,

        @JsonProperty("category_id")
        Long categoryId

) {
}
