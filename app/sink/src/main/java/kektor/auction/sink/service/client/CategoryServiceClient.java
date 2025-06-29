package kektor.auction.sink.service.client;

import kektor.auction.sink.dto.CategoryDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url = "/api", accept = MediaType.APPLICATION_JSON_VALUE)
public interface CategoryServiceClient {

    @GetExchange("/{id}")
    CategoryDto getCategoryById(@PathVariable Long id);

    @GetExchange("/hierarchy/{id}")
    List<Long> getCategoryHierarchyIds(@PathVariable Long id);
}
