package kektor.auction.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;


@Service
@RequiredArgsConstructor
public class QueryService {

    Map<Class<?>, RetrievableService<?, ?>> queryRegistry;

    public QueryService(List<RetrievableService<?, ?>> queryImplList) {
        queryRegistry = queryImplList.stream()
                .collect(toMap(RetrievableService::getType, Function.identity()));
    }

    public <Dto, Filter> List<Dto> getAll(Filter requestFilter, Pageable pageable) {
        var queryImpl = (RetrievableService<Dto, Filter>) queryRegistry.get(requestFilter.getClass());
        return queryImpl.getAll(requestFilter, pageable);
    }

    public <Dto, Filter> Page<Dto> getPage(Filter requestFilter, Pageable pageable) {
        var queryImpl = (RetrievableService<Dto, Filter>) queryRegistry.get(requestFilter.getClass());
        return queryImpl.getPage(requestFilter, pageable);
    }

    public <Dto, Filter> void streamAll(ResponseBodyEmitter emitter, Filter requestFilter, Pageable pageable) {
        var queryImpl = (RetrievableService<Dto, Filter>) queryRegistry.get(requestFilter.getClass());
        queryImpl.streamAll(emitter, requestFilter, pageable);
    }
}
