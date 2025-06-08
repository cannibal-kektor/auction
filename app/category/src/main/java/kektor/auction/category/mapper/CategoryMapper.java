package kektor.auction.category.mapper;

import kektor.auction.category.dto.CategoryDto;
import kektor.auction.category.model.Category;
import org.mapstruct.*;

import java.util.List;


@Mapper(config = MapConfig.class)
public interface CategoryMapper {

    @Mapping(source = "parent.id", target = "parentId")
    CategoryDto toDto(Category category);

//    @Mapping(source = "parentId", target = "parent")
    @InheritInverseConfiguration
    Category toModel(CategoryDto categoryDTO);

    //@Mapping(source = PARENTID, target = PARENT) - сделано так,потому что, если указывать через parent.id, то parent (new Category)
    // всегда будет создаваться и сетаться на MappingTarget(даже если parentId это null) не зависимо от настроенной политики
    @InheritConfiguration
    Category update(CategoryDto categoryDTO, @MappingTarget Category category);

    @Mapping(source = "parentId", target = "id")
    Category idToParent(Long parentId);

    List<CategoryDto> toDto(List<Category> entities);
}
