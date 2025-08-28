package kektor.auction.query.dto;


import java.math.BigDecimal;
import java.time.Instant;

public record PaymentAccountDto(

        Long id,

        BigDecimal balance,

        Long version,

        Long userId,

        Instant registrationDate
) {
}
