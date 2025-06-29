package kektor.auction.sink.service.sync;

import kektor.auction.sink.dto.msg.BidCDC;
import kektor.auction.sink.mapper.CdcMapper;
import kektor.auction.sink.model.Bid;
import kektor.auction.sink.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BidSync implements Sync<BidCDC> {

    final BidRepository repository;
    final CdcMapper mapper;

    @Override
    public void create(BidCDC data) {
        Bid bid = mapper.toModel(data);
        repository.save(bid);
    }

    @Override
    public void update(BidCDC data) {
        Bid bid = mapper.toModel(data);
        repository.save(bid);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Class<BidCDC> getType() {
        return BidCDC.class;
    }

}
