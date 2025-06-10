package kektor.auction.lot.exception;

import lombok.Getter;

import java.time.Instant;

@Getter
public class AuctionAlreadyStartedException extends RuntimeException {

    private static final String ALREADY_STARTED = "Auction on lot with id %d already started at <%s>";

    long lotId;
    Instant auctionStart;

    public AuctionAlreadyStartedException(long lotId, Instant auctionStart) {
        super(String.format(ALREADY_STARTED, lotId, auctionStart));
        this.lotId = lotId;
        this.auctionStart = auctionStart;
    }

}
