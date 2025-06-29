package kektor.auction.sink.service.sync;

import kektor.auction.sink.dto.msg.PaymentAccountCDC;
import kektor.auction.sink.mapper.CdcMapper;
import kektor.auction.sink.model.PaymentAccount;
import kektor.auction.sink.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentSync implements Sync<PaymentAccountCDC> {

    final PaymentRepository repository;
    final CdcMapper mapper;

    @Override
    public void create(PaymentAccountCDC data) {
        PaymentAccount paymentAccount = mapper.toModel(data);
        repository.save(paymentAccount);
    }

    @Override
    public void update(PaymentAccountCDC data) {
        PaymentAccount paymentAccount = mapper.toModel(data);
        repository.save(paymentAccount);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Class<PaymentAccountCDC> getType() {
        return PaymentAccountCDC.class;
    }
}
