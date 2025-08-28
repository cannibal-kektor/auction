package kektor.auction.query.service.impl;

import kektor.auction.query.dto.SagaDto;
import kektor.auction.query.dto.filter.SagaRequestFilter;
import kektor.auction.query.exception.StreamIOException;
import kektor.auction.query.mapper.DtoMapper;
import kektor.auction.query.model.Saga;
import kektor.auction.query.service.RetrievableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SagaQueryService implements RetrievableService<SagaDto, SagaRequestFilter> {

    final ElasticsearchOperations operations;
    final DtoMapper dtoMapper;

    @Override
    public List<SagaDto> getAll(SagaRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        SearchHits<Saga> searchHits = operations.search(query, Saga.class);
        return searchHits.stream()
                .map(SearchHit::getContent)
                .map(dtoMapper::toDto)
                .toList();
    }

    @Override
    public Page<SagaDto> getPage(SagaRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        SearchHits<Saga> searchHits = operations.search(query, Saga.class);
        long totalHits = searchHits.getTotalHits();
        var results = searchHits.stream()
                .map(SearchHit::getContent)
                .map(dtoMapper::toDto)
                .toList();
        return new PageImpl<>(results, pageable, totalHits);
    }

    @Override
    public void streamAll(ResponseBodyEmitter emitter, SagaRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        try (var results = operations.searchForStream(query, Saga.class)) {
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
    public Class<SagaRequestFilter> getType() {
        return SagaRequestFilter.class;
    }

    Query constructQuery(SagaRequestFilter filter, Pageable pageable) {
        Criteria criteria = new Criteria();

        if (filter.sagaStatuses() != null && !filter.sagaStatuses().isEmpty()) {
            criteria.and("status").in(filter.sagaStatuses());
        }

        if (filter.lotsId() != null && !filter.lotsId().isEmpty()) {
            criteria.and("lotId").in(filter.lotsId());
        }

        if (filter.creationTimeLowerLimit() != null || filter.creationTimeUpperLimit() != null) {
            Criteria timeCriteria = new Criteria("creationTime");
            if (filter.creationTimeLowerLimit() != null) {
                timeCriteria.greaterThanEqual(filter.creationTimeLowerLimit());
            }
            if (filter.creationTimeUpperLimit() != null) {
                timeCriteria.lessThanEqual(filter.creationTimeUpperLimit());
            }
            criteria.and(timeCriteria);
        }

        if (filter.biddersId() != null && !filter.biddersId().isEmpty()) {
            criteria.and("bidderId").in(filter.biddersId());
        }

        if (filter.paymentAccountIds() != null && !filter.paymentAccountIds().isEmpty()) {
            criteria.and("paymentAccountId").in(filter.paymentAccountIds());
        }

        if (filter.problemDetail() != null) {
            criteria.and("problemDetail")
                    .matches(filter.problemDetail());
        }

        return new CriteriaQuery(criteria)
                .setPageable(pageable);
    }
}
