package kektor.auction.sink.service;

import kektor.auction.sink.service.sync.Sync;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class DataSynchronizationService {

    static final String CREATE = "c";
    static final String DELETE = "d";
    static final String UPDATE = "u";
    static final String SNAPSHOT = "r";

    Map<Class<?>, Sync<?>> syncMap;

    public DataSynchronizationService(List<Sync<?>> dataSyncs) {
        syncMap = dataSyncs.stream()
                .collect(toMap(Sync::getType, Function.identity()));
    }

    public <T> void syncData(String opType, long id, T msg) {
        Sync<T> sync = (Sync<T>) syncMap.get(msg.getClass());
        switch (opType) {
            case CREATE, SNAPSHOT -> sync.create(msg);
            case UPDATE -> sync.update(msg);
            case DELETE -> sync.delete(id);
        }
    }

    //TODO
    public void resynchronize() {
    }
}
