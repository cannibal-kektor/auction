package kektor.auction.category.aspect;


import kektor.auction.category.dto.CategoryDto;
import kektor.auction.category.dto.CategoryEventMessage;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Aspect
@Order(LOWEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class PublishingEventsAspect {

    final ApplicationEventPublisher eventPublisher;

    @Pointcut("within(kektor.auction.category.service.*)")
    public void inService() {
    }

    @Pointcut("inService() && @annotation(event)")
    public void categoryEventGeneratingMethod(PublishEvent event) {
    }

    @AfterReturning(
            pointcut = "categoryEventGeneratingMethod(event)",
            returning = "retVal", argNames = "retVal,event")
    public CategoryDto categoryCreateUpdateAdvice(CategoryDto retVal, PublishEvent event) {
        var eventMessage = new CategoryEventMessage(event.value(), retVal);
        eventPublisher.publishEvent(eventMessage);
        return retVal;
    }

    @AfterReturning(
            pointcut = "categoryEventGeneratingMethod(event) && args(id)"
            , argNames = "id,event")
    public void categoryDeleteAdvice(Long id, PublishEvent event) {
        var eventMessage = new CategoryEventMessage(
                event.value(),
                CategoryDto.builder()
                        .id(id)
                        .build()
        );
        eventPublisher.publishEvent(eventMessage);
    }

}
