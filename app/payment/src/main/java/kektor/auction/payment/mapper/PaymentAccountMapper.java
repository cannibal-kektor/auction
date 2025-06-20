package kektor.auction.payment.mapper;

import kektor.auction.payment.dto.PaymentAccountDto;
import kektor.auction.payment.model.PaymentAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(config = MapConfig.class, uses = BalanceOperationMapper.class)
public interface PaymentAccountMapper {

    @Mapping(target = "operations", ignore = true)
    PaymentAccountDto toDto(PaymentAccount paymentAccount);

    PaymentAccountDto toDtoWithOps(PaymentAccount paymentAccount);

}
