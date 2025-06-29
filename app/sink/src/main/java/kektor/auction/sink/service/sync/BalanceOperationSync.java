package kektor.auction.sink.service.sync;

import kektor.auction.sink.dto.msg.BalanceOperationCDC;
import kektor.auction.sink.mapper.CdcMapper;
import kektor.auction.sink.model.BalanceOperation;
import kektor.auction.sink.repository.BalanceOperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceOperationSync implements Sync<BalanceOperationCDC> {

    final BalanceOperationRepository repository;
    final CdcMapper mapper;

    @Override
    public void create(BalanceOperationCDC data) {
        BalanceOperation operation = mapper.toModel(data);
        repository.save(operation);
    }

    @Override
    public void update(BalanceOperationCDC data) {
        BalanceOperation operation = mapper.toModel(data);
        repository.save(operation);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Class<BalanceOperationCDC> getType() {
        return BalanceOperationCDC.class;
    }
}
