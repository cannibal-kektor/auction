package kektor.auction.sink.service.sync;

import jakarta.annotation.PostConstruct;
import kektor.auction.sink.dto.CategoryDto;
import kektor.auction.sink.dto.msg.LotCategoriesCDC;
import kektor.auction.sink.service.CategoryCacheableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.ScriptType;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.script.Script;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LotCategorySync implements Sync<LotCategoriesCDC> {

    final CategoryCacheableService categoryService;
    final ElasticsearchOperations operations;

    static final String ADD_CATEGORY_SCRIPT = "add-category";
    static final String REMOVE_CATEGORY_SCRIPT = "remove-category";
    static final String PAINLESS_SCRIPT_LANG = "painless";

    @Override
    public void create(LotCategoriesCDC data) {
        CategoryDto category = categoryService.getCategoryById(data.categoryId());
        List<Long> hierarchyIds = categoryService.getCategoryHierarchyIds(data.categoryId());
        var params = Map.of("newCategory", category, "newCategoryHierarchyIds", hierarchyIds);
        UpdateQuery query = buildUpdateQuery(data.lotId(), ADD_CATEGORY_SCRIPT, params, true);
        operations.update(query, IndexCoordinates.of("lots"));
    }

    @Override
    public void delete(Long id) {
        List<Long> hierarchyIds = categoryService.getCategoryHierarchyIds(id);
        var params = Map.of("categoryId", id, "removeCategoryHierarchyIds", hierarchyIds);
        UpdateQuery query = buildUpdateQuery(id, REMOVE_CATEGORY_SCRIPT, params, false);
        operations.update(query, IndexCoordinates.of("lots"));
    }

    @Override
    public void update(LotCategoriesCDC data) {
    }

    @Override
    public Class<LotCategoriesCDC> getType() {
        return LotCategoriesCDC.class;
    }

    @PostConstruct
    public void initScripts() {
        operations.putScript(constructAddCategoryScript());
        operations.putScript(constructRemoveCategoryScript());
    }

    UpdateQuery buildUpdateQuery(Long id, String scriptName, Map<String, Object> params, boolean upsert) {
        return UpdateQuery.builder(id.toString())
                .withScriptName(scriptName)
                .withScriptType(ScriptType.STORED)
                .withScriptedUpsert(upsert)
                .withParams(params)
                .withRefreshPolicy(RefreshPolicy.WAIT_UNTIL)
                .withRetryOnConflict(3)
                .build();
    }

    Script constructAddCategoryScript() {
        return Script.builder()
                .withId(ADD_CATEGORY_SCRIPT)
                .withLanguage(PAINLESS_SCRIPT_LANG)
                .withSource("""
                        if (ctx.op == 'create') {
                            ctx._source = [:];
                        }
                        if (ctx._source.categories == null) {
                            ctx._source.categories = [];
                            ctx._source.categoryHierarchyIds = [];
                        }
                        boolean exists = false;
                        for (def existing : ctx._source.categories) {
                            if (existing.id == params.newCategory.id) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            ctx._source.categories.add(params.newCategory);
                        }
                        ctx._source.categoryHierarchyIds.addAll(params.newCategoryHierarchyIds);
                        """)
                .build();
    }

    Script constructRemoveCategoryScript() {
        return Script.builder()
                .withId(REMOVE_CATEGORY_SCRIPT)
                .withLanguage(PAINLESS_SCRIPT_LANG)
                .withSource("""
                        if (ctx._source == null || ctx._source.categories == null) {
                            return;
                        }
                        def iterator = ctx._source.categories.iterator();
                        while (iterator.hasNext()) {
                            def item = iterator.next();
                            if (item.id==params.categoryId) {
                                iterator.remove();
                            }
                        }
                        ctx._source.categoryHierarchyIds.removeAll(params.removeCategoryHierarchyIds);
                        """)
                .build();
    }
}
