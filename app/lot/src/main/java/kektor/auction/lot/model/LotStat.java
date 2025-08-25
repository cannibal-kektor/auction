package kektor.auction.lot.model;


import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
public class LotStat {

    @NotNull
    BigDecimal highestBid = BigDecimal.ZERO;

    @NotNull
    Long winnerId = 0L;

    @NotNull
    Long winningBidId = 0L;

    @NotNull
    Long bidsCount = 0L;

}
