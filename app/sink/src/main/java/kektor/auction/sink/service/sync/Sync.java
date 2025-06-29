package kektor.auction.sink.service.sync;

public interface Sync<T> {

    void create(T data);

    void update(T data);

    void delete(Long id);

    Class<T> getType();

}
