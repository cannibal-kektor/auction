package kektor.auction.query.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.*;

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

        @MultiField(
                mainField = @Field(type = FieldType.Keyword),
                otherFields = {
                        @InnerField(suffix = "autocomplete", type = FieldType.Text,
                                analyzer = "autocomplete_analyzer")
                }
        )
        String name,

        @Field(type = FieldType.Text, analyzer = "ru_eng")
        String description,

        LotStatus status,

        Long sellerId,

        Long sellerPaymentAccountId,

        @Field(type = FieldType.Scaled_Float, scalingFactor = 10000)
        BigDecimal initialPrice,

        Instant auctionStart,

        Instant auctionEnd,

        @Field(type = FieldType.Scaled_Float, scalingFactor = 10000)
        BigDecimal highestBid,

        Long winnerId,

        Long winningBidId,

        Long bidsCount,

        @Field(type = FieldType.Nested)
        Set<Category> categories,

        @Field(type = FieldType.Long)
        Set<Long> categoryHierarchyIds
) {

}
