package kektor.auction.sink.service.sync;

import kektor.auction.sink.dto.msg.LotCDC;
import kektor.auction.sink.mapper.CdcMapper;
import kektor.auction.sink.model.Lot;
import kektor.auction.sink.repository.LotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LotSync implements Sync<LotCDC> {

    final LotRepository repository;
    final ElasticsearchOperations operations;
    final CdcMapper mapper;

    @Override
    public void create(LotCDC data) {
        upsertLotData(data);
    }

    @Override
    public void update(LotCDC data) {
        upsertLotData(data);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Class<LotCDC> getType() {
        return LotCDC.class;
    }

    public void upsertLotData(LotCDC data) {
        Lot lot = mapper.toModel(data);
        Document doc = preparePartialDocument(lot);
        UpdateQuery query = UpdateQuery.builder(data.id().toString())
                .withDocument(doc)
                .withDocAsUpsert(true)
                .withRefreshPolicy(RefreshPolicy.WAIT_UNTIL)
                .withRetryOnConflict(3)
                .build();
        operations.update(query, IndexCoordinates.of("lots"));
    }

    Document preparePartialDocument(Lot lot) {
        Document document = operations.getElasticsearchConverter()
                .mapObject(lot);
        document.remove("categories");
        document.remove("categoryHierarchyIds");
        return document;
    }
}
