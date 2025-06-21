package kektor.auction.payment.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public final class NotEnoughAccountFundException extends RuntimeException {

    private static final String NOT_ENOUGH_FUND = "Not enough fund for the bid. Current account [Account Id = %d] amount= %.2f . Requested bid amount = %.2f";

    long accountId;
    BigDecimal currentAmount;
    BigDecimal requestedAmount;

    public NotEnoughAccountFundException(Long accountId, BigDecimal currentAmount
            , BigDecimal requestedAmount) {
        super(String.format(NOT_ENOUGH_FUND, accountId, currentAmount, requestedAmount));
        this.accountId = accountId;
        this.currentAmount = currentAmount;
        this.requestedAmount = requestedAmount;
    }


}
