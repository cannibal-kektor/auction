package kektor.auction.query.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;

public interface RetrievableService<Dto, Filter> {

    List<Dto> getAll(Filter requestFilter, Pageable pageable);

    Page<Dto> getPage(Filter requestFilter, Pageable pageable);

    void streamAll(ResponseBodyEmitter emitter, Filter requestFilter, Pageable pageable);

    Class<Filter> getType();
}
