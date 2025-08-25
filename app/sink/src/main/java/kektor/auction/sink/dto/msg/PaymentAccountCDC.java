package kektor.auction.sink.dto.msg;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentAccountCDC(

        Long id,

        BigDecimal balance,

        Long version,

        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("registration_date")
        Instant registrationDate
) {
}
