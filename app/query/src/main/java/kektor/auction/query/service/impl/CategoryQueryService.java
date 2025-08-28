package kektor.auction.query.service.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import kektor.auction.query.dto.CategoryDto;
import kektor.auction.query.dto.filter.CategoryRequestFilter;
import kektor.auction.query.exception.StreamIOException;
import kektor.auction.query.mapper.DtoMapper;
import kektor.auction.query.model.Category;
import kektor.auction.query.model.Lot;
import kektor.auction.query.service.RetrievableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryQueryService implements RetrievableService<CategoryDto, CategoryRequestFilter> {

    final ElasticsearchOperations operations;
    final DtoMapper dtoMapper;

    @Override
    public List<CategoryDto> getAll(CategoryRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        SearchHits<Category> searchHits = operations.search(query, Category.class);
        return searchHits.stream()
                .map(SearchHit::getContent)
                .map(dtoMapper::toDto)
                .toList();
    }

    @Override
    public Page<CategoryDto> getPage(CategoryRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        SearchHits<Category> searchHits = operations.search(query, Category.class);
        long totalHits = searchHits.getTotalHits();
        var results = searchHits.stream()
                .map(SearchHit::getContent)
                .map(dtoMapper::toDto)
                .toList();
        return new PageImpl<>(results, pageable, totalHits);
    }

    @Override
    public void streamAll(ResponseBodyEmitter emitter, CategoryRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        try (var results = operations.searchForStream(query, Category.class)) {
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
    public Class<CategoryRequestFilter> getType() {
        return CategoryRequestFilter.class;
    }

    Query constructQuery(CategoryRequestFilter filter, Pageable pageable) {
        Set<Long> filteredCategoryIds = findCategoryIdsByBaseFilter(filter);
        Map<Long, Long> categoryLotCounts = calculateLotCounts(filter, filteredCategoryIds);
        Set<Long> finalCategoryIds = filterByLotCount(filter, categoryLotCounts);
        if (finalCategoryIds.isEmpty()) {
            return null;
        }
        Criteria criteria = new Criteria("id").in(finalCategoryIds);
        return new CriteriaQuery(criteria)
                .setPageable(pageable);
    }

    private Set<Long> findCategoryIdsByBaseFilter(CategoryRequestFilter filter) {
        Criteria criteria = new Criteria();

        if (filter.name() != null) {
            criteria.and("name").is(filter.name());
        }

        if (filter.parentId() != null && !filter.parentId().isEmpty()) {
            criteria.and("parentId").in(filter.parentId());
        }

        var query = new CriteriaQuery(criteria);
        //only id
        query.addFields("id");

        return operations.search(query, Category.class)
                .stream()
                .map(hit -> hit.getContent().id())
                .collect(Collectors.toSet());
    }

    private Map<Long, Long> calculateLotCounts(CategoryRequestFilter filter, Set<Long> categoryIds) {
        if (categoryIds.isEmpty()) {
            return Collections.emptyMap();
        }
//        QueryBuilders.q
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> {
                    var boolBuilder = new BoolQuery.Builder();
                    if (filter.statuses() != null && !filter.statuses().isEmpty()) {
                        boolBuilder.filter(f -> f.terms(t -> t
                                .field("status")
                                .terms(terms -> terms.value(
                                        filter.statuses()
                                                .stream()
                                                .map(Enum::name)
                                                .map(FieldValue::of)
                                                .toList()))
                        ));
                    }
                    boolBuilder.filter(f -> f.terms(t -> t
                            .field("categoryHierarchyIds")
                            .terms(terms -> terms.value(
                                    categoryIds.stream()
                                            .map(String::valueOf)
                                            .map(FieldValue::of)
                                            .toList()))
                    ));
                    return q.bool(boolBuilder.build());
                })
                .withAggregation("category_counts",
                        Aggregation.of(a -> a.terms(t -> t
                                        .field("categoryHierarchyIds")
                                )
                        )
                )
                .withPageable(Pageable.ofSize(0)) // Only aggregation data
                .build();

        SearchHits<Lot> searchHits = operations.search(query, Lot.class);

        return ((ElasticsearchAggregations) searchHits.getAggregations())
                .get("category_counts")
                .aggregation()
                .getAggregate()
                .sterms()
                .buckets()
                .array()
                .stream()
                .collect(Collectors.toMap(bucket ->
                        bucket.key().longValue(), StringTermsBucket::docCount));
        //Long.parseLong(bucket.key())
    }

    private Set<Long> filterByLotCount(CategoryRequestFilter filter, Map<Long, Long> categoryLotCounts) {
        return categoryLotCounts
                .entrySet()
                .stream()
                .filter(entry ->
                        filter.numOfLotsLowerLimit() == null || entry.getValue() >= filter.numOfLotsLowerLimit()
                )
                .filter(entry ->
                        filter.numOfLotsUpperLimit() == null || entry.getValue() <= filter.numOfLotsUpperLimit())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

}
