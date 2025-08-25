package kektor.auction.category.service;

import jakarta.validation.constraints.Positive;
import kektor.auction.category.aspect.PublishEvent;
import kektor.auction.category.dto.CategoryDto;
import kektor.auction.category.dto.CategoryEventMessage;
import kektor.auction.category.mapper.CategoryMapper;
import kektor.auction.category.repository.CategoryRepository;
import kektor.auction.category.exception.CategoryNotFoundException;
import kektor.auction.category.exception.RestrictParentDeletionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    final CategoryRepository categoryRepository;
    final CategoryMapper mapper;

    public CategoryDto get(Long categoryId) {
        var optionalCategory = categoryRepository.findById(categoryId);
        return optionalCategory
                .map(mapper::toDto)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    @PublishEvent(CategoryEventMessage.EventType.CREATED)
    @Transactional
    public CategoryDto create(CategoryDto createDTO) {
        var category = categoryRepository.save(mapper.toModel(createDTO));
        return mapper.toDto(category);
    }

    @PublishEvent(CategoryEventMessage.EventType.UPDATED)
    @Transactional
    public CategoryDto update(Long categoryId, CategoryDto updateDTO) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    mapper.update(updateDTO, category);
                    return mapper.toDto(category);
                })
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    @PublishEvent(CategoryEventMessage.EventType.DELETED)
    @Transactional
    public void delete(Long id) {
        int deletedCount = categoryRepository.deleteByIdIfNoChildren(id);
        if (deletedCount == 0) {
            if (!categoryRepository.existsById(id)) {
                throw new CategoryNotFoundException(id);
            }
            throw new RestrictParentDeletionException(id);
        }
    }

    public List<CategoryDto> getBulk(List<Long> categoryIds) {
        var allById = categoryRepository.findAllById(categoryIds);
        return mapper.toDto(allById);
    }


    public List<Long> getHierarchy( Long categoryId) {
        return categoryRepository.findCategoryHierarchyIds(categoryId);
    }

    public List<CategoryDto> getAll() {
        var all = categoryRepository.findAll();
        return mapper.toDto(all);
    }
}
