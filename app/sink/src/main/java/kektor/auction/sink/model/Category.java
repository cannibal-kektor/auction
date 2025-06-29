package kektor.auction.sink.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Set;


@Builder
@TypeAlias("category")
@Document(indexName = "categories")
@Setting(
        sortFields = {"name"},
        sortOrders = {Setting.SortOrder.asc})
public record Category(

        @Id
        Long id,

        Long parentId,

        @Field(type = FieldType.Keyword)
        String name,

        @Field(type = FieldType.Long)
        Set<Long> categoryHierarchyIds
) {


}
