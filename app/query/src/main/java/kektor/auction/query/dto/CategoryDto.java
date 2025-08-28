package kektor.auction.query.dto;

import kektor.auction.query.model.LotStatus;

import java.util.Set;

public record CategoryDto(

        Long id,

        Long parentId,

        String name,

        Long numOfLots,

        Set<Long> categoryHierarchyIds
) {
}
