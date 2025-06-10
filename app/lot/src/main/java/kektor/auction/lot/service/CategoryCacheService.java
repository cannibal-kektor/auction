package kektor.auction.lot.service;

import com.github.benmanes.caffeine.cache.Cache;
import kektor.auction.lot.dto.CategoryDto;
import kektor.auction.lot.repository.LotRepository;
import kektor.auction.lot.service.client.CategoryServiceClient;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Service
@CacheConfig(cacheNames = CategoryCacheService.CACHE_NAME)
public class CategoryCacheService {

    public static final String CACHE_NAME = "categories";

    final CategoryServiceClient categoryClient;
    final Cache<Long, CategoryDto> cache;
    final LotRepository lotRepository;

    @SuppressWarnings("unchecked")
    public CategoryCacheService(CategoryServiceClient categoryClient,
                                CaffeineCacheManager cacheManager, LotRepository lotRepository) {
        this.categoryClient = categoryClient;
        CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(CACHE_NAME);
        Object obj = caffeineCache.getNativeCache();
        this.cache = (Cache<Long, CategoryDto>) obj;
        this.lotRepository = lotRepository;
    }

    @Cacheable
    public CategoryDto getCategoryById(Long id) {
        return categoryClient.getCategoryById(id);
    }

    @CacheEvict
    public void evictCategoryById(Long id) {
    }

    @Cacheable(key = "'all'")
    public List<CategoryDto> getAllCategories() {
        return categoryClient.getAllCategories();
    }

    public Set<CategoryDto> getCategories(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptySet();
        }
        var result = cache.getAllPresent(ids);
        Set<Long> missingIds = ids.stream()
                .filter(id -> !result.containsKey(id))
                .collect(toSet());
        if (!missingIds.isEmpty()) {
            try {
                var loaded = categoryClient.getCategoriesBulk(missingIds);
                loaded.forEach(category -> {
                    cache.put(category.id(), category);
                    result.put(category.id(), category);
                });
                var staleList = missingIds
                        .stream()
                        .filter(id -> !result.containsKey(id))
                        .peek(id -> {
//                    log.warn("Category {} not found", id);
                            result.put(id, new CategoryDto(id, "Unknown"));
                        })
                        .toList();

                if (!staleList.isEmpty())
                    triggerDeleteStale(staleList);

            } catch (RestClientResponseException ex) {
                result.putAll(missingIds.stream()
                        .map(id -> new CategoryDto(id, "Unknown"))
                        .collect(toMap(CategoryDto::id, Function.identity())));
            }
        }

        return new HashSet<>(result.values());
    }


    @CacheEvict(allEntries = true)
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void refreshAllCategories() {
        var freshCategories = categoryClient.getAllCategories();
        freshCategories.forEach(cat -> cache.put(cat.id(), cat));
        var currentCategoryIds = lotRepository.findDistinctCategoryIds();
        var freshCategoriesSet = freshCategories.stream()
                .map(CategoryDto::id)
                .collect(toSet());
        var toDelete = currentCategoryIds
                .stream()
                .filter(id -> !freshCategoriesSet.contains(id))
                .toList();
        if (!toDelete.isEmpty())
            triggerDeleteStale(toDelete);
    }


    public void triggerDeleteStale(List<Long> staleIds) {
        lotRepository.deleteStaleCategoryIds(staleIds);
    }


}

