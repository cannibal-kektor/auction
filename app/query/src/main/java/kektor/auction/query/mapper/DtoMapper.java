package kektor.auction.query.mapper;

import kektor.auction.query.dto.*;
import kektor.auction.query.model.*;
import org.mapstruct.Mapper;
import org.springframework.http.ProblemDetail;

@Mapper(config = MapConfig.class)
public interface DtoMapper {

    default String convertToString(ProblemDetail detail) {
        return detail.toString();
    }

    BalanceOperationDto toDto(BalanceOperation operation);

    BidDto toDto(Bid bid);

    CategoryDto toDto(Category category);

    LotDto toDto(Lot lot);

    SagaDto toDto(Saga saga);

    PaymentAccountDto toDto(PaymentAccount account);

}
