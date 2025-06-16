package kektor.auction.bid.exception;


import lombok.Getter;

@Getter
public class BidConcurrencyException extends RuntimeException {

    private static final String BID_CONCURRENCY_LOST = "There is already new fresh bid(s) for the lot Lot Id = %d retired Lot Version = %d. Saga Id = %d";
    long lotId;
    long sagaId;
    long lotVersion;

    public BidConcurrencyException(Long lotId, Long lotVersion, Long sagaId) {
        super(String.format(BID_CONCURRENCY_LOST, lotId, lotVersion, sagaId));
        this.lotId = lotId;
        this.sagaId = sagaId;
        this.lotVersion = lotVersion;
    }
}
