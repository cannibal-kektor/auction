package kektor.auction.payment.exception;


import lombok.Getter;

@Getter
public class PaymentAccountNotFoundByIdException extends RuntimeException {

    private static final String NOT_FOUND = "Payment Account not found by identifier = %d";

    long accountId;

    public PaymentAccountNotFoundByIdException(long accountId) {
        super(String.format(NOT_FOUND, accountId));
        this.accountId = accountId;
    }
}
