package kektor.auction.payment.exception;


import lombok.Getter;

@Getter
public class PaymentAccountNotFoundByUserIdException extends RuntimeException {

    private static final String NOT_FOUND = "Payment Account not found by user identifier = %d";

    long userId;

    public PaymentAccountNotFoundByUserIdException(long userId) {
        super(String.format(NOT_FOUND, userId));
        this.userId = userId;
    }
}
