package kektor.auction.orchestrator.mapper;


import kektor.auction.orchestrator.dto.LotDto;
import kektor.auction.orchestrator.dto.NewBidRequestDto;
import kektor.auction.orchestrator.dto.BidCreateDto;
import kektor.auction.orchestrator.model.Saga;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(config = MapConfig.class)
public interface SagaMapper {

    @Mapping(source = "bid.amount", target = "newBidAmount")
    @Mapping(source = "bid.bidderId", target = "bidderId")
    @Mapping(source = "lot.highestBid", target = "compensateBidAmount")
    @Mapping(source = "lot.winningBidId", target = "compensateWinningBidId")
    @Mapping(source = "lot.id", target = "lotId")
    @Mapping(source = "lot.version", target = "lotVersion")
    @Mapping(source = "creationTime", target = "createdOn")
    Saga toModel(NewBidRequestDto bid, LotDto lot, Instant creationTime);

    @Mapping(source = "newBidAmount", target = "amount")
    BidCreateDto toBid(Saga saga);

}
