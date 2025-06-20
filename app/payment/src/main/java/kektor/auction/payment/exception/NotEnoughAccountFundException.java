package kektor.auction.payment.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public final class NotEnoughAccountFundException extends RuntimeException {

    private static final String NOT_ENOUGH_FUND = "Not enough fund for the bid [Bid Id = %d]. Current account [Account Id = %d] amount= %.2f . Requested bid amount = %.2f";

    long bidId;
    long accountId;
    BigDecimal currentAmount;
    BigDecimal requestedAmount;

    public NotEnoughAccountFundException(Long bidId, Long accountId, BigDecimal currentAmount
            , BigDecimal requestedAmount) {
        super(String.format(NOT_ENOUGH_FUND, bidId, accountId, currentAmount, requestedAmount));
        this.bidId = bidId;
        this.accountId = accountId;
        this.currentAmount = currentAmount;
        this.requestedAmount = requestedAmount;
    }


}
