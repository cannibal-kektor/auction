package kektor.auction.lot.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
public class LotStat {

    //    @Formula("select s.max_bid from ITEM_SUMMARY s where s.item_id=id")
//    @ColumnDefault("0.00")
    @Column(updatable = false, nullable = false)
    BigDecimal highestBid = BigDecimal.ZERO;

    //    @Formula("select s.bid_count from ITEM_SUMMARY s where s.item_id=id")
//    @ColumnDefault("0")
    @Column(updatable = false, nullable = false)
    Long bidsCount = 0L;

}
