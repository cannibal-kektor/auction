package kektor.auction.category.service;

import kektor.auction.category.aspect.PublishEvent;
import kektor.auction.category.dto.CategoryDto;
import kektor.auction.category.dto.CategoryEventMessage;
import kektor.auction.category.mapper.CategoryMapper;
import kektor.auction.category.model.Category;
import kektor.auction.category.repository.CategoryRepository;
import kektor.auction.category.exception.ResourceNotFoundException;
import kektor.auction.category.exception.RestrictParentDeletionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
//@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryService {

    final CategoryRepository categoryRepository;
    final CategoryMapper mapper;

    public CategoryDto get(Long categoryId) {
        var optionalCategory = categoryRepository.findById(categoryId);
        return optionalCategory
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
    }

    @PublishEvent(CategoryEventMessage.EVENT_TYPE.CREATED)
    @Transactional
    public CategoryDto create(CategoryDto createDTO) {
        var category = categoryRepository.save(mapper.toModel(createDTO));
        return mapper.toDto(category);
    }

    @PublishEvent(CategoryEventMessage.EVENT_TYPE.UPDATED)
    @Transactional
    public CategoryDto update(Long categoryId, CategoryDto updateDTO) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    mapper.update(updateDTO, category);
                    return mapper.toDto(category);
                })
                .orElseThrow(() -> new ResourceNotFoundException(Category.class, categoryId));
    }

    @PublishEvent(CategoryEventMessage.EVENT_TYPE.DELETED)
    @Transactional
    public void delete(Long id) {
        int deletedCount = categoryRepository.deleteByIdIfNoChildren(id);
        if (deletedCount == 0) {
            if (!categoryRepository.existsById(id)) {
                throw new ResourceNotFoundException(Category.class, id);
            }
            throw new RestrictParentDeletionException(id);
        }
    }

    public List<CategoryDto> getBulk(List<Long> categoryIds) {
        var allById = categoryRepository.findAllById(categoryIds);
        return mapper.toDto(allById);
    }


    public List<CategoryDto> getAll() {
        var all = categoryRepository.findAll();
        return mapper.toDto(all);
    }
}
