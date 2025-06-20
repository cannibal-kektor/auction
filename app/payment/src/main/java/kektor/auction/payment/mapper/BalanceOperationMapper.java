package kektor.auction.payment.mapper;

import kektor.auction.payment.dto.*;
import kektor.auction.payment.model.BalanceOperation;
import kektor.auction.payment.model.CreditOperation;
import kektor.auction.payment.model.DebitOperation;
import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;

@Mapper(config = MapConfig.class)
public interface BalanceOperationMapper {

    @SubclassMapping(source = DebitOperation.class, target = DebitOperationDto.class)
    @SubclassMapping(source = CreditOperation.class, target = CreditOperationDto.class)
    BalanceOperationDto toDto(BalanceOperation operation);

    DebitOperation toModel(ReplenishmentDto replenishmentDto);

    CreditOperation toModel(ReservationDto reservationDto);
}
