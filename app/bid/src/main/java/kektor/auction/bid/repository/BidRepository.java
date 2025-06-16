package kektor.auction.bid.repository;

import kektor.auction.bid.model.Bid;
import kektor.auction.bid.model.BidStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

//    @Query(name = "Bid.findItemMaxBid")
//    BigDecimal findItemMaxBid(Long itemId);


//    @QueryHints(value = {

    /// /            @QueryHint(name = HINT_FETCH_SIZE, value = "" + 1),
    /// /            @QueryHint(name = HINT_CACHEABLE, value = "false"),
//            @QueryHint(name = HINT_READ_ONLY, value = "true")

    Optional<Bid> findBySagaId(Long sagaId);

}
