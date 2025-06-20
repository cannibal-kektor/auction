package kektor.auction.payment.dto;

import java.math.BigDecimal;
import java.time.Instant;

public interface BalanceOperationDto {

    Long id();

    BigDecimal amount();

    Instant timestamp();
}
