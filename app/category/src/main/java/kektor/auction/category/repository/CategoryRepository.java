package kektor.auction.category.repository;

import kektor.auction.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = """
        WITH RECURSIVE category_hierarchy AS (
            SELECT id, parent_id
            FROM category
            WHERE id = :categoryId
        
            UNION ALL
        
            SELECT c.id, c.parent_id
            FROM category c
            INNER JOIN category_hierarchy ch ON ch.parent_id = c.id
        )
        SELECT id FROM category_hierarchy
        """, nativeQuery = true)
    List<Long> findCategoryHierarchyIds(@Param("categoryId") Long categoryId);

    @Modifying
    @Query("DELETE FROM Category c WHERE c.id = :id AND NOT EXISTS (SELECT 1 FROM Category child WHERE child.parent.id = :id)")
    int deleteByIdIfNoChildren(@Param("id") Long id);

}
