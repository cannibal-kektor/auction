package kektor.auction.orchestrator.exception;


import lombok.Getter;

@Getter
public class ConcurrentSagaException extends RuntimeException {

    private static final String CONCURRENT_SAGA = "There is already an active concurrent saga on this lot";

    public ConcurrentSagaException() {
        super(CONCURRENT_SAGA);
    }
}
