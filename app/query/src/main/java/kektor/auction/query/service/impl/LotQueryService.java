package kektor.auction.query.service.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.util.ObjectBuilder;
import kektor.auction.query.dto.LotDto;
import kektor.auction.query.dto.filter.LotRequestFilter;
import kektor.auction.query.exception.StreamIOException;
import kektor.auction.query.mapper.DtoMapper;
import kektor.auction.query.model.Lot;
import kektor.auction.query.service.RetrievableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LotQueryService implements RetrievableService<LotDto, LotRequestFilter> {

    final ElasticsearchOperations operations;
    final DtoMapper dtoMapper;

    @Override
    public List<LotDto> getAll(LotRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        SearchHits<Lot> searchHits = operations.search(query, Lot.class);
        return searchHits.stream()
                .map(SearchHit::getContent)
                .map(dtoMapper::toDto)
                .toList();
    }

    @Override
    public Page<LotDto> getPage(LotRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        SearchHits<Lot> searchHits = operations.search(query, Lot.class);
        long totalHits = searchHits.getTotalHits();
        var results = searchHits.stream()
                .map(SearchHit::getContent)
                .map(dtoMapper::toDto)
                .toList();
        return new PageImpl<>(results, pageable, totalHits);
    }

    @Override
    public void streamAll(ResponseBodyEmitter emitter, LotRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        try (var results = operations.searchForStream(query, Lot.class)) {
            results.stream()
                    .map(SearchHit::getContent)
                    .map(dtoMapper::toDto)
                    .forEach(dto -> {
                        try {
                            emitter.send(dto);
                        } catch (Exception e) {
                            emitter.completeWithError(e);
                            throw new StreamIOException("IO Streaming Error", e);
                        }
                    });
            emitter.complete();
        }
    }

    @Override
    public Class<LotRequestFilter> getType() {
        return LotRequestFilter.class;
    }


    Query constructQuery(LotRequestFilter filter, Pageable pageable) {

        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

        if (filter.name() != null && !filter.name().isEmpty()) {
            boolBuilder.must(QueryBuilders.match(m -> m
                    .field("name.autocomplete")
                    .query(filter.name())
                    .analyzer("autocomplete_search")
                    .fuzziness("1")
                    .prefixLength(2)
                    .operator(Operator.And)
            ));
        }

        if (filter.description() != null && !filter.description().isEmpty()) {
            boolBuilder.must(QueryBuilders.match(m -> m
                    .field("description")
                    .query(filter.description())
                    .analyzer("ru_eng")
            ));
        }

        if (filter.statuses() != null && !filter.statuses().isEmpty()) {
            boolBuilder.filter(QueryBuilders.terms(t -> t
                    .field("status")
                    .terms(terms -> terms.value(filter.statuses().stream()
                            .map(Enum::name)
                            .map(FieldValue::of)
                            .toList()))
            ));
        }

        if (filter.sellersId() != null && !filter.sellersId().isEmpty()) {
            boolBuilder.filter(QueryBuilders.terms(t -> t
                    .field("sellerId")
                    .terms(terms -> terms.value(filter.sellersId().stream()
                            .map(FieldValue::of)
                            .toList()))
            ));
        }

        if (filter.winnersIds() != null && !filter.winnersIds().isEmpty()) {
            boolBuilder.filter(QueryBuilders.terms(t -> t
                    .field("winnerId")
                    .terms(terms -> terms.value(filter.winnersIds().stream()
                            .map(FieldValue::of)
                            .toList()))
            ));
        }

        if (filter.categoriesId() != null && !filter.categoriesId().isEmpty()) {
            boolBuilder.filter(QueryBuilders.terms(t -> t
                    .field("categoryHierarchyIds")
                    .terms(terms -> terms.value(filter.categoriesId().stream()
                            .map(FieldValue::of)
                            .toList()))
            ));
        }

        addRangeQuery(boolBuilder, "initialPrice",
                filter.initialPriceLowerLimit(),
                filter.initialPriceUpperLimit());

        addRangeQuery(boolBuilder, "auctionStart",
                filter.auctionStartLowerLimit(),
                filter.auctionStartUpperLimit());

        addRangeQuery(boolBuilder, "auctionEnd",
                filter.auctionEndLowerLimit(),
                filter.auctionEndUpperLimit());

        addRangeQuery(boolBuilder, "highestBid",
                filter.highestBidLowerLimit(),
                filter.highestBidUpperLimit());

        addRangeQuery(boolBuilder, "bidsCount",
                filter.bidsCountLowerLimit(),
                filter.bidsCountUpperLimit());


        return NativeQuery.builder()
                .withQuery(q -> q.bool(boolBuilder.build()))
                .withPageable(pageable)
                .build();
    }

    private void addRangeQuery(BoolQuery.Builder boolBuilder, String fieldName,
                               Object lower, Object upper) {
        if (lower != null || upper != null) {
            ObjectBuilder<RangeQuery> rangeBuilder = QueryBuilders.range().term(
                    builder -> {
                        builder.field(fieldName);
                        if (lower != null) {
                            builder.gte(lower.toString());
                        }
                        if (upper != null) {
                            builder.lte(upper.toString());
                        }
                        return builder;
                    }
            );
            boolBuilder.filter(rangeBuilder.build());
        }
    }


}
