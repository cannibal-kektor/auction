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
@TypeAlias("saga")
@Document(indexName = "sagas")
@Setting(settingPath = "elastic/text-analyze.json",
        sortFields = {"creationTime"},
        sortOrders = {Setting.SortOrder.desc})
public record Saga(

        @Id
        Long sagaId,

        SagaStatus status,

        Long lotId,

        Instant creationTime,

        Long bidderId,

        Long paymentAccountId,

        @Field(type = FieldType.Scaled_Float, scalingFactor = 10000)
        BigDecimal newBidAmount,

        @Field(type = FieldType.Scaled_Float, scalingFactor = 10000)
        BigDecimal compensateBidAmount,

        Long compensateWinningBidId,

        Long compensateWinnerId,

        @Field(type = FieldType.Text, analyzer = "ru_eng")
        String problemDetail
) {
}