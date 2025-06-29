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
import java.util.Set;


@Builder
@TypeAlias("lot")
@Document(indexName = "lots")
@Setting(settingPath = "elastic/text-analyze.json",
        sortFields = {"auctionStart"},
        sortOrders = {Setting.SortOrder.desc})
public record Lot(

        @Id
        Long id,

        Long version,

        @Field(type = FieldType.Keyword)
        String name,

        LotStatus status,

        @Field(type = FieldType.Text, analyzer = "ru_eng")
        String description,

        Long sellerId,

        @Field(type = FieldType.Scaled_Float, scalingFactor = 10000)
        BigDecimal initialPrice,

        Instant auctionStart,

        Instant auctionEnd,

        @Field(type = FieldType.Scaled_Float, scalingFactor = 10000)
        BigDecimal highestBid,

        Long winningBidId,

        Long bidsCount,

        @Field(type = FieldType.Nested)
        Set<Category> categories,

        @Field(type = FieldType.Long)
        Set<Long> categoryHierarchyIds
) {


}
