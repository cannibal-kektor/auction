package kektor.auction.category.repository;

import kektor.auction.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(name = "Category.findHierarchy", nativeQuery = true)
    List<Category> getHierarchy();

    @Procedure(name = "Category.refreshCategoryStatistic")
    void refreshCategoryStatistics();

//    @Query(name = "Category.findHierarchyIds", nativeQuery = true)
//    List<Long> getHierarchyIds(@Param("categoriesId") List<Long> categoriesId);

//    default List<Category> findFiltered(CategoryRequestFilter requestFilter, Pageable pageable){
//        List<Category> categories;
//        if (requestFilter.includeItemsCount() != null && requestFilter.includeItemsCount()) {
//            List<Tuple> categoriesWithItemsCount = findCategoriesWithItemsCount(requestFilter.name(), requestFilter.parentId(),
//                    requestFilter.itemsCountLowerLimit(), requestFilter.itemsCountUpperLimit(), pageable);
//
//            categories =categoriesWithItemsCount
//                    .stream()
//                    .map(tuple -> {
//                        Category category = tuple.get(0, Category.class);
//                        Long numOfItems = tuple.get(1, Long.class);
//                        category.setNumOfItems(numOfItems);
//                        return category;
//                    }).toList();
//        } else {
////            Predicate predicate = constructPredicate(requestFilter);
//            categories = findCategories(requestFilter.name(), requestFilter.parentId(), pageable);
//        }
//        return categories;
//    }
//
//    default Predicate constructPredicate(CategoryRequestFilter requestFilter){
//        BooleanBuilder predicate = new BooleanBuilder();
//        if (requestFilter.name() != null) {
//            predicate.and(category.name.containsIgnoreCase(requestFilter.name()));
//        }
//        if (requestFilter.parentId() != null) {
//            if (requestFilter.parentId() != 0L) {
//                predicate.and(category.parent.id.eq(requestFilter.parentId()));
//            } else {
//                predicate.and(category.parent.id.isNull());
//            }
//        }
//        return predicate;
//    }

//    where (:categoryName is null or lower(name) like concat('%', :categoryName, '%'))
//    and (:parentId is null or
//            case
//            when :parentId = 0 then parent_id is null
//            else parent_id = :parentId
//            end)

//    @Query(name = "Category.findCategoriesWithCountOfIncludingItems", nativeQuery = true)
//    List<Tuple> findCategoriesWithItemsCount(@Param("categoryName") String name, @Param("parentId") Long parentId,
//                                             @Param("itemsCountLowerLimit") Long itemsCountLowerLimit, @Param("itemsCountUpperLimit") Long itemsCountUpperLimit,
//                                             Pageable pageable);
//    @Query(name = "Category.findCategories", nativeQuery = true)
//    List<Category> findCategories(@Param("categoryName") String name, @Param("parentId") Long parentId, Pageable pageable);


}
