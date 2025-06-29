package kektor.auction.sink.dto.msg;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PaymentAccountCDC(

        Long id,

        BigDecimal balance,

        Long version,

        @JsonProperty("user_id")
        Long userId
) {
}
