package kektor.auction.query.model;


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
@TypeAlias("paymentAccount")
@Document(indexName = "paymentAccounts")
@Setting(
        sortFields = {"id"},
        sortOrders = {Setting.SortOrder.asc})
public record PaymentAccount(

        @Id
        Long id,

        @Field(type = FieldType.Scaled_Float, scalingFactor = 10000)
        BigDecimal balance,

        Long version,

        Long userId,

        Instant registrationDate
) {
}
