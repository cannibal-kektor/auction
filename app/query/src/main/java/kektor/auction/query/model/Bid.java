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
@TypeAlias("bid")
@Document(indexName = "bids")
@Setting(
        sortFields = {"creationTime"},
        sortOrders = {Setting.SortOrder.desc})
public record Bid(

        @Id
        Long id,

        Long lotId,

        Long bidderId,

        Long paymentAccountId,

        Long sagaId,

        @Field(type = FieldType.Scaled_Float, scalingFactor = 10000)
        BigDecimal amount,

        Instant creationTime,

        BidStatus status) {

}
