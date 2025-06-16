package kektor.auction.bid.exception;


import lombok.Getter;

@Getter
public class BidNotFoundBySagaException extends RuntimeException {

    private static final String NOT_FOUND = "Bid not found by saga identifier = %d";
    long sagaId;

    public BidNotFoundBySagaException(long sagaId) {
        super(String.format(NOT_FOUND, sagaId));
        this.sagaId = sagaId;
    }
}
