package kektor.auction.payment.conf;


import kektor.auction.payment.exception.handler.ApplicationUncaughtExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

@Configuration
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

    final ApplicationUncaughtExceptionHandler asyncExceptionHandler;

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return asyncExceptionHandler;
    }

}


