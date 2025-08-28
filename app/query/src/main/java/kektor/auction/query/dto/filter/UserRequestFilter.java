package kektor.auction.query.dto.filter;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.Instant;

public record UserRequestFilter(

        @Size(min = 4, max = 20)
        String username,

//        @Email
        @Size(min = 3, max = 30)
        String email,

        @Size(max = 20)
        String role,

        Boolean enabled,

        @PositiveOrZero
        Long totalNumOfBidsLowerLimit,

        @PositiveOrZero
        Long totalNumOfBidsUpperLimit,

//        @JsonInclude
        @PositiveOrZero
        BigDecimal highestBidLowerLimit,

        @PositiveOrZero
        BigDecimal highestBidUpperLimit,

//        @JsonInclude
        @Past
        Instant latestBidTimeLowerLimit,

        Instant latestBidTimeUpperLimit,

        @PositiveOrZero
        Long totalNumOfOwnedAuctionItemsLowerLimit,

        @PositiveOrZero
        Long totalNumOfOwnedAuctionItemsUpperLimit,

        @PositiveOrZero
        Long numOfActiveAuctionItemsLowerLimit,

        @PositiveOrZero
        Long numOfActiveAuctionItemsUpperLimit,

        @Nullable
        boolean withPrivate,

        @Nullable
        boolean withStatistic
) {
}
