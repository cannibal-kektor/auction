package kektor.auction.sink.repository;


import kektor.auction.sink.model.BalanceOperation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceOperationRepository extends ElasticsearchRepository<BalanceOperation, Long> {

}
