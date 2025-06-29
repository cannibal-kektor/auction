package kektor.auction.sink.dto.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import kektor.auction.sink.model.CreditStatus;
import kektor.auction.sink.model.OperationType;

import java.math.BigDecimal;
import java.time.Instant;

public record BalanceOperationCDC(

        Long id,

        @JsonProperty("account_id")
        Long accountId,

        BigDecimal amount,

        @JsonProperty("created_at")
        Instant createdAt,

        @JsonProperty("operation_type")
        OperationType operationType,

        @JsonProperty("credit_status")
        CreditStatus status,

        @JsonProperty("bid_id")
        Long bidId,

        @JsonProperty("saga_id")
        Long sagaId
) {
}
