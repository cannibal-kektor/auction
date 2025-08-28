package kektor.auction.query.service.impl;

import kektor.auction.query.dto.PaymentAccountDto;
import kektor.auction.query.dto.filter.PaymentAccountFilter;
import kektor.auction.query.exception.StreamIOException;
import kektor.auction.query.mapper.DtoMapper;
import kektor.auction.query.model.PaymentAccount;
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
public class PaymentAccountQueryService implements RetrievableService<PaymentAccountDto, PaymentAccountFilter> {

    final ElasticsearchOperations operations;
    final DtoMapper dtoMapper;

    @Override
    public List<PaymentAccountDto> getAll(PaymentAccountFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        SearchHits<PaymentAccount> searchHits = operations.search(query, PaymentAccount.class);
        return searchHits.stream()
                .map(SearchHit::getContent)
                .map(dtoMapper::toDto)
                .toList();
    }

    @Override
    public Page<PaymentAccountDto> getPage(PaymentAccountFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        SearchHits<PaymentAccount> searchHits = operations.search(query, PaymentAccount.class);
        long totalHits = searchHits.getTotalHits();
        var results = searchHits.stream()
                .map(SearchHit::getContent)
                .map(dtoMapper::toDto)
                .toList();
        return new PageImpl<>(results, pageable, totalHits);
    }

    @Override
    public void streamAll(ResponseBodyEmitter emitter, PaymentAccountFilter filter, Pageable pageable) {
        Query query = constructQuery(filter, pageable);
        try (var results = operations.searchForStream(query, PaymentAccount.class)) {
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
    public Class<PaymentAccountFilter> getType() {
        return PaymentAccountFilter.class;
    }

    Query constructQuery(PaymentAccountFilter filter, Pageable pageable) {
        Criteria criteria = new Criteria();

        if (filter.usersId() != null && !filter.usersId().isEmpty()) {
            criteria.and("userId").in(filter.usersId());
        }

        if (filter.balanceLowerLimit() != null || filter.balanceUpperLimit() != null) {
            Criteria balanceCriteria = new Criteria("balance");
            if (filter.balanceLowerLimit() != null) {
                balanceCriteria.greaterThanEqual(filter.balanceLowerLimit());
            }
            if (filter.balanceUpperLimit() != null) {
                balanceCriteria.lessThanEqual(filter.balanceUpperLimit());
            }
            criteria.and(balanceCriteria);
        }

        if (filter.registrationDateLowerLimit() != null || filter.registrationDateUpperLimit() != null) {
            Criteria timeCriteria = new Criteria("registrationDate");
            if (filter.registrationDateLowerLimit() != null) {
                timeCriteria.greaterThanEqual(filter.registrationDateLowerLimit());
            }
            if (filter.registrationDateUpperLimit() != null) {
                timeCriteria.lessThanEqual(filter.registrationDateUpperLimit());
            }
            criteria.and(timeCriteria);
        }

        return new CriteriaQuery(criteria)
                .setPageable(pageable);
    }

}
