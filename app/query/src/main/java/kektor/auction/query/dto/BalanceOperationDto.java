package kektor.auction.query.dto;

import kektor.auction.query.model.CreditStatus;
import kektor.auction.query.model.OperationType;

import java.math.BigDecimal;
import java.time.Instant;

public record BalanceOperationDto(

        Long id,

        Long accountId,

        BigDecimal amount,

        Instant createdAt,

        OperationType operationType,

        CreditStatus status,

        Long bidId,

        Long sagaId
) {
}
