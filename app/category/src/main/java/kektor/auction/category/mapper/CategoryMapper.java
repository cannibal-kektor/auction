package kektor.auction.category.mapper;

import kektor.auction.category.dto.CategoryDto;
import kektor.auction.category.model.Category;
import org.mapstruct.*;

import java.util.List;


@Mapper(config = MapConfig.class)
public interface CategoryMapper {

    @Mapping(source = "parent.id", target = "parentId")
    CategoryDto toDto(Category category);

    @Mapping(source = "parentId", target = "parent")
    Category toModel(CategoryDto categoryDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    Category update(CategoryDto categoryDTO, @MappingTarget Category category);

    @Mapping(source = "parentId", target = "id")
    Category idToParent(Long parentId);

    List<CategoryDto> toDto(List<Category> entities);
}
