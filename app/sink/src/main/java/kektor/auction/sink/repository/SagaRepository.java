package kektor.auction.sink.repository;


import kektor.auction.sink.model.Saga;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SagaRepository extends ElasticsearchRepository<Saga, Long> {

}
