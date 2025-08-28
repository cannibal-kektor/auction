package kektor.auction.query.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SortingParametersValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSortingParameters {

//    @AliasFor("value")
//    SortType type();

    SortType value();

    String message() default "Sorting parameters should be valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    enum SortType {
        BID,
        CATEGORY,
        ITEM,
        USER
    }
}
