package kektor.auction.lot.repository;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import kektor.auction.lot.model.Lot;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LotRepository extends CustomOperation, JpaRepository<Lot, Long> {


    <T> T findById(Long id, Class<T> type);

//    Optional<Lot> findById(Long id);

    @Query(value = "SELECT lot_id FROM lot_categories WHERE categories_id=:categoryId", nativeQuery = true)
    List<Long> findLotsIdsByCategory(@Param("categoryIds") Long categoryId);


    @Meta(comment = "Find lot using optimistic lock")
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<Lot> findItemVersionForceIncrementById(Long id);

    //    @Query("SELECT DISTINCT c FROM Lot l JOIN l.categoriesId c")
    @Query(value = "SELECT DISTINCT categories_id FROM lot_categories", nativeQuery = true)
    Set<Long> findDistinctCategoryIds();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM lot_categories WHERE categories_id IN :categoryIds", nativeQuery = true)
    void deleteStaleCategoryIds(@Param("categoryIds") List<Long> categoryIds);


    @Query(value = "select version from Lot where id=:id")
    Long fetchLotVersion(@Param("id") Long lotId);

//    @Query("select i.id from Item i join i.categories c where c.id in :categoryIds group by i.id having count(i.id) = :#{#categoryIds.size()} ")
//    List<Long> findItemsIdsWithSelectedCategoriesBy(@Param("categoryIds") Collection<Long> categoryIds,

//                                                    Pageable pageable);
//    @QueryHints(value = {
////            @QueryHint(name = HINT_FETCH_SIZE, value = "" + 1),
////            @QueryHint(name = HINT_CACHEABLE, value = "false"),
//            @QueryHint(name = HINT_READ_ONLY, value = "true")
////            @QueryHint(name = HINT_PASS_DISTINCT_THROUGH, value = "false")
//    })
//    @EntityGraph(value = "ItemWithCategories", type = EntityGraph.EntityGraphType.LOAD)
////    @Query("select i from Item i left join fetch i.categories order by i.id")

//    Stream<Item> streamItemsBy(Sort sort);
    //    @EntityGraph(value = "ItemWithCategories", type = EntityGraph.EntityGraphType.LOAD)
//    @Query("select i from Item i left join i.categories c where c.id in :categoryIds group by i having count(i) = :#{#categoryIds.size()} ")

//    Stream<Item> streamItemsWithSelectedCategoriesBy(@Param("categoryIds") Collection<Long> categoryIds, Sort sort);


//    @Query("select i.id, max(b.amount), count(i.id) from Item i" +
//            " left join i.bids b where i in ?1 group by i.id")

//    List<ItemStatistic> fetchItemsStatistics(Collection<Long> longStream);


}
