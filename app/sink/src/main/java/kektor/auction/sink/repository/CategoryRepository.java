package kektor.auction.sink.repository;


import kektor.auction.sink.model.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends ElasticsearchRepository<Category, Long> {

}
