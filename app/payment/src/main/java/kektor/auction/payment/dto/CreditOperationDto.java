package kektor.auction.payment.dto;

import kektor.auction.payment.model.CreditOperation;

import java.math.BigDecimal;
import java.time.Instant;

public record CreditOperationDto(
        Long id,
        BigDecimal amount,
        Instant createdAt,
        CreditOperation.Status status,
        Long bidId,
        Long sagaId
) implements BalanceOperationDto {
}
