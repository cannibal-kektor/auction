package kektor.auction.sink.service;

import kektor.auction.sink.dto.CategoryDto;
import kektor.auction.sink.service.client.CategoryServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryCacheableService {

    public static final String CATEGORY_CACHE = "categories";
    public static final String CATEGORY_HIERARCHY_IDS_CACHE = "categoryHierarchyIds";

    final CategoryServiceClient categoryClient;

    @Cacheable(cacheNames = CATEGORY_CACHE)
    public CategoryDto getCategoryById(Long id) {
        return categoryClient.getCategoryById(id);
        //TODO catchException
    }

    @Cacheable(cacheNames = CATEGORY_HIERARCHY_IDS_CACHE)
    public List<Long> getCategoryHierarchyIds(Long id) {
        return categoryClient.getCategoryHierarchyIds(id);
    }

    @CacheEvict(cacheNames = {CATEGORY_CACHE, CATEGORY_HIERARCHY_IDS_CACHE})
    public void evictCategoryCache(Long id) {
    }


}

