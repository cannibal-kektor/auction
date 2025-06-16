package kektor.auction.bid.dto;

import kektor.auction.bid.model.BidStatus;

public record BidMessage(
        BidStatus bidStatus,
        OrchestratedBidDto orchestratedBidDto
) {
}
