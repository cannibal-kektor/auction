package kektor.auction.query.service.impl;

import kektor.auction.query.dto.BalanceOperationDto;
import kektor.auction.query.dto.filter.BalanceOpRequestFilter;
import kektor.auction.query.exception.StreamIOException;
import kektor.auction.query.mapper.DtoMapper;
import kektor.auction.query.model.BalanceOperation;
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
public class BalanceOperationQueryService implements RetrievableService<BalanceOperationDto, BalanceOpRequestFilter> {

    final ElasticsearchOperations operations;
    final DtoMapper dtoMapper;

    @Override
    public List<BalanceOperationDto> getAll(BalanceOpRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        SearchHits<BalanceOperation> searchHits = operations.search(query, BalanceOperation.class);
        return searchHits.stream()
                .map(SearchHit::getContent)
                .map(dtoMapper::toDto)
                .toList();
    }

    @Override
    public Page<BalanceOperationDto> getPage(BalanceOpRequestFilter requestFilter, Pageable pageable) {
        Query query = constructQuery(requestFilter, pageable);
        SearchHits<BalanceOperation> searchHits = operations.search(query, BalanceOperation.class);
        long totalHits = searchHits.getTotalHits();
        var results = searchHits.stream()
                .map(SearchHit::getContent)
                .map(dtoMapper::toDto)
                .toList();
        return new PageImpl<>(results, pageable, totalHits);
    }


    @Override
    public void streamAll(ResponseBodyEmitter emitter, BalanceOpRequestFilter requestFilter, Pageable pageable) {
        Query query = constructQuery(requestFilter, pageable);
        try (var results = operations.searchForStream(query, BalanceOperation.class)) {
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
    public Class<BalanceOpRequestFilter> getType() {
        return BalanceOpRequestFilter.class;
    }


    Query constructQuery(BalanceOpRequestFilter filter, Pageable pageable) {
        Criteria criteria = new Criteria();

        if (filter.accountIds() != null && !filter.accountIds().isEmpty()) {
            criteria.and("accountId").in(filter.accountIds());
        }

        if (filter.amountLowerLimit() != null || filter.amountUpperLimit() != null) {
            Criteria amountCriteria = new Criteria("amount");
            if (filter.amountLowerLimit() != null) {
                amountCriteria.greaterThanEqual(filter.amountLowerLimit());
            }
            if (filter.amountUpperLimit() != null) {
                amountCriteria.lessThanEqual(filter.amountUpperLimit());
            }
            criteria.and(amountCriteria);
        }

        if (filter.createdAtLowerLimit() != null || filter.createdAtUpperLimit() != null) {
            Criteria dateCriteria = new Criteria("createdAt");
            if (filter.createdAtLowerLimit() != null) {
                dateCriteria.greaterThanEqual(filter.createdAtLowerLimit());
            }
            if (filter.createdAtUpperLimit() != null) {
                dateCriteria.lessThanEqual(filter.createdAtUpperLimit());
            }
            criteria.and(dateCriteria);
        }

        if (filter.operationTypes() != null && !filter.operationTypes().isEmpty()) {
            criteria.and("operationType").in(filter.operationTypes());
        }

        if (filter.creditStatuses() != null && !filter.creditStatuses().isEmpty()) {
            criteria.and("status").in(filter.creditStatuses());
        }

        if (filter.bidsId() != null && !filter.bidsId().isEmpty()) {
            criteria.and("bidId").in(filter.bidsId());
        }

        return new CriteriaQuery(criteria)
                .setPageable(pageable);
    }

}
