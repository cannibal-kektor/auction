package kektor.auction.category.service.impl;

import kektor.auction.category.dto.CategoryDto;
import kektor.auction.category.mapper.CategoryMapper;
import kektor.auction.category.model.Category;
import kektor.auction.category.repository.CategoryRepository;
import kektor.auction.category.service.CategoryService;
import kektor.auction.category.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
//@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper mapper;

    @Override
    public CategoryDto get(Long categoryId) {
        var optionalCategory = categoryRepository.findById(categoryId);
        return optionalCategory
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
    }

    @Transactional
    @Override
    public CategoryDto create(CategoryDto createDTO) {
        var category = categoryRepository.save(mapper.toModel(createDTO));
        return mapper.toDto(category);
    }

    @Transactional
    @Override
    public CategoryDto update(Long categoryId, CategoryDto updateDTO) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    mapper.update(updateDTO, category);
                    return mapper.toDto(category);
                })
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
    }


}
