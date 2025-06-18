package kektor.auction.orchestrator.exception;


import lombok.Getter;

import java.time.Instant;

@Getter
public class ConcurrentSagaException extends RuntimeException {

    private static final String CONCURRENT_SAGA = "There is already an active concurrent saga on this lot. Lot id = %d. Instant = %s";

    public long lotId;
    public Instant triedCreatedOn;


    public ConcurrentSagaException(long lotId, Instant triedCreatedOn) {
        super(CONCURRENT_SAGA.formatted(lotId, triedCreatedOn.toString()));
        this.lotId = lotId;
        this.triedCreatedOn = triedCreatedOn;
    }
}
