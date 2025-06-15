package kektor.auction.orchestrator.mapper;


import kektor.auction.orchestrator.dto.LotDto;
import kektor.auction.orchestrator.dto.BidRequestDto;
import kektor.auction.orchestrator.dto.BidDto;
import kektor.auction.orchestrator.model.Saga;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(config = MapConfig.class)
public interface SagaMapper {

    @Mapping(source = "bid.amount", target = "newBidAmount")
    @Mapping(source = "lot.highestBid", target = "compensateHighestBid")
    @Mapping(source = "lot.bidsCount", target = "compensateBidsCount")
    @Mapping(source = "creationTime", target = "createdOn")
    Saga toModel(BidRequestDto bid, LotDto lot, Instant creationTime);

    @Mapping(source = "newBidAmount", target = "amount")
    BidDto toBid(Saga saga);

}
