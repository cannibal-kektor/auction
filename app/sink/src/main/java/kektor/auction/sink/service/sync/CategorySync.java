package kektor.auction.sink.service.sync;

import jakarta.annotation.PostConstruct;
import kektor.auction.sink.dto.msg.CategoryCDC;
import kektor.auction.sink.mapper.CdcMapper;
import kektor.auction.sink.model.Category;
import kektor.auction.sink.repository.CategoryRepository;
import kektor.auction.sink.service.CategoryCacheableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.ScriptType;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.script.Script;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CategorySync implements Sync<CategoryCDC> {

    final CategoryRepository repository;
    final CategoryCacheableService categoryService;
    final ElasticsearchOperations operations;
    final CdcMapper mapper;

    static final String UPDATE_CATEGORY_NAME_SCRIPT = "update-category";
    static final String PAINLESS_SCRIPT_LANG = "painless";

    @Override
    public void create(CategoryCDC data) {
        List<Long> hierarchyIds = categoryService.getCategoryHierarchyIds(data.id());
        Category category = mapper.toModel(data, hierarchyIds);
        repository.save(category);
    }

    @Override
    public void update(CategoryCDC data) {
        List<Long> hierarchyIds = categoryService.getCategoryHierarchyIds(data.id());
        Category category = mapper.toModel(data, hierarchyIds);
        repository.save(category);
        UpdateQuery updateQuery = buildUpdateCategoryNamesQuery(data);
        operations.update(updateQuery, IndexCoordinates.of("lots"));
        categoryService.evictCategoryCache(data.id());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
        categoryService.evictCategoryCache(id);
    }

    @Override
    public Class<CategoryCDC> getType() {
        return CategoryCDC.class;
    }

    @PostConstruct
    public void initScripts() {
        operations.putScript(constructUpdateCategoryNameScript());
    }


    UpdateQuery buildUpdateCategoryNamesQuery(CategoryCDC data) {
        Query query = buildSearchNestedCategoryQuery(data.id());
        return UpdateQuery.builder(query)
                .withScriptName(UPDATE_CATEGORY_NAME_SCRIPT)
                .withScriptType(ScriptType.STORED)
                .withParams(Map.of("categoryId", data.id(), "newName", data.name()))
                .withRefreshPolicy(RefreshPolicy.WAIT_UNTIL)
                .withRetryOnConflict(3)
                .build();
    }


    /*
      {
        "query": {
          "nested": {
            "path": "categories",
            "query": {
              "term": {
                "categories.id": "{{categoryId}}"
              }
            }
          }
        }
      }
    */
    Query buildSearchNestedCategoryQuery(Long searchId) {
        return NativeQuery.builder()
                .withQuery(q ->
                        q.nested(nested -> nested
                                .path("categories")
                                .query(nestedQ -> nestedQ
                                        .term(termQ -> termQ
                                                .field("categories.id")
                                                .value(searchId)
                                        )
                                )
                        )
                )
                .build();
    }

    Script constructUpdateCategoryNameScript() {
        return Script.builder()
                .withId(UPDATE_CATEGORY_NAME_SCRIPT)
                .withLanguage(PAINLESS_SCRIPT_LANG)
                .withSource("""
                        if (ctx._source == null || ctx._source.categories == null) {
                            return;
                        }
                        for (def cat : ctx._source.categories) {
                            if (cat.id == params.categoryId) {
                                cat.name = params.newName;
                                found = true;
                                break;
                            }
                        }
                        """)
                .build();
    }

}
