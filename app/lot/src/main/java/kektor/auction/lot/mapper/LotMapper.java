package kektor.auction.lot.mapper;


import kektor.auction.lot.dto.CategoryDto;
import kektor.auction.lot.dto.LotCreateDto;
import kektor.auction.lot.dto.LotDto;
import kektor.auction.lot.dto.LotUpdateDto;
import kektor.auction.lot.model.Lot;
import org.mapstruct.*;

import java.util.Set;

@Mapper(config = MapConfig.class
        , collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
public interface LotMapper {

    @Mapping(source = "lot", target = ".")
    @Mapping(source = "lot.lotStat.bidsCount", target = "bidsCount")
    @Mapping(source = "lot.lotStat.highestBid", target = "highestBid")
    @Mapping(source = "lot.lotStat.winningBidId", target = "winningBidId")
    @Mapping(source = "categories", target = "categories")
    LotDto toDto(Lot lot, Set<CategoryDto> categories);

    Lot toModel(LotCreateDto createDTO);

    @Mapping(target = "version", ignore = true)
    Lot update(LotUpdateDto updateDTO, @MappingTarget Lot lot);

    @Mapping(target = "id", source = "categoryId")
    CategoryDto toDtoCategory(Long categoryId);

    default Set<CategoryDto> identity(Set<CategoryDto> categories){
        return categories;
    }

}
