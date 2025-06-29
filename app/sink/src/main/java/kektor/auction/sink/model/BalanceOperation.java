package kektor.auction.sink.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.math.BigDecimal;
import java.time.Instant;


@Builder
@TypeAlias("balanceOperation")
@Document(indexName = "balanceOperations")
@Setting(
        sortFields = {"createdAt"},
        sortOrders = {Setting.SortOrder.desc})
public record BalanceOperation(

        @Id
        Long id,

        Long accountId,

        @Field(type = FieldType.Scaled_Float, scalingFactor = 10000)
        BigDecimal amount,

        Instant createdAt,

        OperationType operationType,

        CreditStatus status,

        Long bidId,

        Long sagaId
) {
}
