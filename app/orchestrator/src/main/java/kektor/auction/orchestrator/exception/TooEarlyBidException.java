package kektor.auction.orchestrator.exception;

import lombok.Getter;

import java.time.Instant;

@Getter
public final class TooEarlyBidException extends RuntimeException {
    private static final String BID_TOO_EARLY = "Bid attempt is too early!. Item auction is starting at %s. Bid attempt at %s ";

    long lotId;
    Instant bidAttemptTime;
    Instant auctionStart;

    public TooEarlyBidException(Long lotId, Instant bidAttemptTime, Instant auctionStart) {
        super(String.format(BID_TOO_EARLY, auctionStart, bidAttemptTime));
        this.lotId = lotId;
        this.bidAttemptTime = bidAttemptTime;
        this.auctionStart = auctionStart;
    }

}
