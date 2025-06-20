package kektor.auction.payment.dto;


import java.math.BigDecimal;
import java.util.Set;

public record PaymentAccountDto(
        Long id,
        BigDecimal balance,
        Long version,
        Long userId,
        Set<BalanceOperationDto> operations
) {
}
