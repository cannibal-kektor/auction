package kektor.auction.lot.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kektor.auction.lot.dto.LotCreateDto;
import kektor.auction.lot.dto.LotUpdateDto;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class AuctionDatesValidator implements ConstraintValidator<AuctionDatesValid, Object> {

    String AUCTION_END_LATER = "Auction end must be later than auction start";
    String AUCTION_DURATION_AT_LEAST_HOUR = "Auction duration must me at least one hour";

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {

        Instant auctionStart;
        Instant auctionEnd;
        switch (dto){
            case LotCreateDto createDto -> {
                auctionStart = createDto.auctionStart();
                auctionEnd = createDto.auctionEnd();
            }
            case LotUpdateDto updateDto -> {
                auctionStart = updateDto.auctionStart();
                auctionEnd = updateDto.auctionEnd();
            }
            default -> throw new IllegalStateException("Unexpected value: " + dto);
        }

        if (!auctionStart.isBefore(auctionEnd)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(AUCTION_END_LATER)
                    .addConstraintViolation();
            return false;
        }

        if (Duration.between(auctionStart, auctionEnd).toHours() == 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(AUCTION_DURATION_AT_LEAST_HOUR)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
