package kektor.auction.orchestrator.exception;


import lombok.Getter;

//@ApplicationException(rollback = true)

@Getter
public abstract sealed class InvalidBidException extends RuntimeException
        permits StaleItemVersionException, TooLowBidAmountException, TooLateBidException, TooEarlyBidException {

    Long lotId;

    public InvalidBidException(String message, Long lotId) {
        super(message);
        this.lotId = lotId;
    }
}
