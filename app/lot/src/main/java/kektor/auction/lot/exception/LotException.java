package kektor.auction.lot.exception;


import lombok.Getter;

//@ApplicationException(rollback = true)

@Getter
public abstract sealed class LotException extends RuntimeException
        permits StaleLotVersionException {

    Long lotId;

    public LotException(String message, Long lotId) {
        super(message);
        this.lotId = lotId;
    }
}
