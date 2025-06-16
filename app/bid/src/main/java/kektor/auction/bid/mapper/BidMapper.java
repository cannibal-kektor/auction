package kektor.auction.bid.mapper;

import kektor.auction.bid.dto.BidDto;
import kektor.auction.bid.dto.OrchestratedBidDto;
import kektor.auction.bid.model.Bid;
import org.mapstruct.Mapper;


@Mapper(config = MapConfig.class)
public interface BidMapper {

    BidDto toDto(Bid bid);

    Bid toModel(OrchestratedBidDto bidDto);
}
