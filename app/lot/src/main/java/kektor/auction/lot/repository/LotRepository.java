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

    @Query(value = "SELECT lot_id FROM lot_categories WHERE categories_id=:categoryId", nativeQuery = true)
    List<Long> findLotsIdsByCategory(@Param("categoryIds") Long categoryId);

    @Query(value = "SELECT DISTINCT categories_id FROM lot_categories", nativeQuery = true)
    Set<Long> findDistinctCategoryIds();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM lot_categories WHERE categories_id IN :categoryIds", nativeQuery = true)
    void deleteStaleCategoryIds(@Param("categoryIds") List<Long> categoryIds);

    @Query(value = "select version from Lot where id=:id")
    Long fetchLotVersion(@Param("id") Long lotId);

}
