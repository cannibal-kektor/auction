package kektor.auction.lot.service.client;

import kektor.auction.lot.dto.CategoryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(url = "/api", accept = MediaType.APPLICATION_JSON_VALUE)
public interface CategoryServiceClient {

    @GetExchange("/{id}")
    CategoryDto getCategoryById(@PathVariable Long id);

    @GetExchange
    List<CategoryDto> getAllCategories();

    @GetExchange("/bulk/{ids}")
    List<CategoryDto> getCategoriesBulk(@PathVariable("ids") Set<Long> ids);
}
