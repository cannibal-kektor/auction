package kektor.auction.bid.exception;


import lombok.Getter;

@Getter
public class BidNotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "Bid not found by identifier = %d";

    long bidId;

    public BidNotFoundException(long bidId) {
        super(String.format(NOT_FOUND, bidId));
        this.bidId = bidId;
    }
}
