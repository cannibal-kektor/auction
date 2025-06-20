package kektor.auction.payment.exception;


import lombok.Getter;

@Getter
public class OperationNotFoundBySagaIdException extends RuntimeException {

    private static final String NOT_FOUND = "Payment operation not found by saga identifier = %d";
    long sagaId;

    public OperationNotFoundBySagaIdException(long sagaId) {
        super(String.format(NOT_FOUND, sagaId));
        this.sagaId = sagaId;
    }
}
