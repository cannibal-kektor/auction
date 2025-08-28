package kektor.auction.query.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import java.lang.reflect.Method;

@Slf4j
@Component
public class ApplicationUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler, ErrorHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.warn("Exception while invoking async method. Exception:[{}] Message:[{}] Method: [{}]",
                ex.getClass().getSimpleName(), ex.getMessage(), method);
    }

    @Override
    public void handleError(Throwable ex) {
        log.warn("Exception while invoking scheduled method. Exception:[{}] Message:[{}]",
                ex.getClass().getSimpleName(), ex.getMessage());
    }
}
