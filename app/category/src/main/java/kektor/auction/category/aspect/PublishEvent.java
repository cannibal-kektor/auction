package kektor.auction.category.aspect;

import kektor.auction.category.dto.CategoryEventMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PublishEvent {

    CategoryEventMessage.EventType value();

}
