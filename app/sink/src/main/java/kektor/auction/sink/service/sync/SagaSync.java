package kektor.auction.sink.service.sync;

import kektor.auction.sink.dto.msg.SagaCDC;
import kektor.auction.sink.mapper.CdcMapper;
import kektor.auction.sink.model.Saga;
import kektor.auction.sink.repository.SagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SagaSync implements Sync<SagaCDC> {

    final SagaRepository repository;
    final CdcMapper mapper;

    @Override
    public void create(SagaCDC data) {
        Saga saga = mapper.toModel(data);
        repository.save(saga);
    }

    @Override
    public void update(SagaCDC data) {
        Saga saga = mapper.toModel(data);
        repository.save(saga);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Class<SagaCDC> getType() {
        return SagaCDC.class;
    }
}
