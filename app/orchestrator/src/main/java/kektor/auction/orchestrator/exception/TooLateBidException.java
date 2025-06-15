package kektor.auction.orchestrator.exception;

import lombok.Getter;

import java.time.Instant;

@Getter
public final class TooLateBidException extends InvalidBidException {
    private static final String BID_TOO_LATE = "Bid attempt is too late!. Item auction ended at %s. Bid attempt at %s ";

    Instant bidAttemptTime;

    Instant auctionEnd;


    public TooLateBidException(Long lotId, Instant bidAttemptTime, Instant auctionEnd) {
        super(String.format(BID_TOO_LATE, auctionEnd, bidAttemptTime), lotId);
        this.bidAttemptTime = bidAttemptTime;
        this.auctionEnd = auctionEnd;
    }

}
