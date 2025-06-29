package kektor.auction.sink.repository;


import kektor.auction.sink.model.Bid;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends ElasticsearchRepository<Bid, Long> {

}
