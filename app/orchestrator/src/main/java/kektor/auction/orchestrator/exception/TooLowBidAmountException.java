package kektor.auction.orchestrator.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public final class TooLowBidAmountException extends InvalidBidException {

    private static final String BID_TOO_LOW = "Bid amount too low! Minimal bid amount must be greater than = %.2f . Suggested = %.2f";

    BigDecimal lowerLimitBidAmountExclusive;

    BigDecimal attemptAmount;

    public TooLowBidAmountException(Long lotId, BigDecimal lowerLimitBidAmountExclusive
            , BigDecimal attemptAmount) {
        super(String.format(BID_TOO_LOW, lowerLimitBidAmountExclusive, attemptAmount), lotId);
        this.lowerLimitBidAmountExclusive = lowerLimitBidAmountExclusive;
        this.attemptAmount = attemptAmount;
    }


}
