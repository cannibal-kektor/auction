package kektor.auction.lot.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AuctionDatesValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuctionDatesValid {

    String message() default "Auction dates should be valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
