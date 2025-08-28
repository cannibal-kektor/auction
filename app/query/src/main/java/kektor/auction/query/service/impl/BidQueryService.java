package kektor.auction.query.service.impl;

import kektor.auction.query.dto.BidDto;
import kektor.auction.query.dto.filter.BidRequestFilter;
import kektor.auction.query.exception.StreamIOException;
import kektor.auction.query.mapper.DtoMapper;
import kektor.auction.query.model.Bid;
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
public class BidQueryService implements RetrievableService<BidDto, BidRequestFilter> {

    final ElasticsearchOperations operations;
    final DtoMapper dtoMapper;

    @Override
    public List<BidDto> getAll(BidRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        SearchHits<Bid> searchHits = operations.search(query, Bid.class);
        return searchHits.stream()
                .map(SearchHit::getContent)
                .map(dtoMapper::toDto)
                .toList();
    }

    @Override
    public Page<BidDto> getPage(BidRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        SearchHits<Bid> searchHits = operations.search(query, Bid.class);
        long totalHits = searchHits.getTotalHits();
        var results = searchHits.stream()
                .map(SearchHit::getContent)
                .map(dtoMapper::toDto)
                .toList();
        return new PageImpl<>(results, pageable, totalHits);
    }

    @Override
    public void streamAll(ResponseBodyEmitter emitter, BidRequestFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        try (var results = operations.searchForStream(query, Bid.class)) {
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
    public Class<BidRequestFilter> getType() {
        return BidRequestFilter.class;
    }

    Query constructQuery(BidRequestFilter filter, Pageable pageable) {
        Criteria criteria = new Criteria();

        if (filter.lotsId() != null && !filter.lotsId().isEmpty()) {
            criteria.and("lotId").in(filter.lotsId());
        }

        if (filter.biddersId() != null && !filter.biddersId().isEmpty()) {
            criteria.and("bidderId").in(filter.biddersId());
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

        if (filter.statuses() != null && !filter.statuses().isEmpty()) {
            criteria.and("status").in(filter.statuses());
        }

        return new CriteriaQuery(criteria)
                .setPageable(pageable);
    }
}
