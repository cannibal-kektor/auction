package kektor.auction.payment.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record DebitOperationDto(
        Long id,
        BigDecimal amount,
        Instant createdAt
) implements BalanceOperationDto {
}
