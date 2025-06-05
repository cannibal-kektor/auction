package kektor.auction.category.service;

import kektor.auction.category.dto.CategoryDto;
import org.springframework.transaction.annotation.Transactional;

public interface CategoryService {

    CategoryDto get(Long categoryId);

    @Transactional
    CategoryDto create(CategoryDto.Request categoryDTO);

    @Transactional
    CategoryDto update(Long categoryId, CategoryDto.Request categoryDTO);


}
