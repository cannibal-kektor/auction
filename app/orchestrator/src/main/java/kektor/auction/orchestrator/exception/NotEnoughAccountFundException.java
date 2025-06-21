package kektor.auction.orchestrator.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public final class NotEnoughAccountFundException extends RuntimeException {

    private static final String NOT_ENOUGH_FUND = "The User [User id: %d] doesnt have enough funds for the bid with amount = %.2f";

    long userId;
    BigDecimal requestedAmount;

    public NotEnoughAccountFundException( Long userId, BigDecimal requestedAmount) {
        super(String.format(NOT_ENOUGH_FUND, userId, requestedAmount));
        this.userId = userId;
        this.requestedAmount = requestedAmount;
    }


}
