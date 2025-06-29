package kektor.auction.sink.repository;

import kektor.auction.sink.model.Lot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepository extends ElasticsearchRepository<Lot, Long> {

}
