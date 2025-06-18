package kektor.auction.category.repository;

import kektor.auction.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(name = "Category.findHierarchy", nativeQuery = true)
    List<Category> getHierarchy();

    @Modifying
    @Query("DELETE FROM Category c WHERE c.id = :id AND NOT EXISTS (SELECT 1 FROM Category child WHERE child.parent.id = :id)")
    int deleteByIdIfNoChildren(@Param("id") Long id);

}
