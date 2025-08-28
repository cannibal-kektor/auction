package kektor.auction.query.dto.filter;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import kektor.auction.query.model.LotStatus;

import java.util.Set;

public record CategoryRequestFilter(

        @Size(min = 3, max = 20)
        String name,

        @PositiveOrZero
        Set<Long> parentId,

        @PositiveOrZero
        Long numOfLotsLowerLimit,

        @PositiveOrZero
        Long numOfLotsUpperLimit,

        Set<LotStatus> statuses

) {

}
