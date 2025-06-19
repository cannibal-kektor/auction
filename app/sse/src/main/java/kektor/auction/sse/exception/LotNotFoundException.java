package kektor.auction.sse.exception;


import lombok.Getter;

@Getter
public class LotNotFoundException extends RuntimeException {

    private static final String NOT_FOUND = "Lot not found by identifier = %d";

    long lotId;

    public LotNotFoundException(long lotId) {
        super(String.format(NOT_FOUND, lotId));
        this.lotId = lotId;
    }
}
