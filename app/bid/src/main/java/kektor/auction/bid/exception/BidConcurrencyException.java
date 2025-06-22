package kektor.auction.bid.exception;


import kektor.auction.bid.dto.BidCreateDto;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class BidConcurrencyException extends RuntimeException {

    private static final String CONCURRENT_BID = "There is already an new bid on this lot. Bid Request : %s";

    Long sagaId;
    long lotId;
    Long bidderId;
    Instant triedCreatedOn;
    BigDecimal amount;

    public BidConcurrencyException(BidCreateDto bidDto) {
        super(CONCURRENT_BID.formatted(bidDto.toString()));
    }
}
