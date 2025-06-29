package kektor.auction.sink.conf;


import kektor.auction.sink.exception.handler.ApplicationUncaughtExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.task.ThreadPoolTaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

    final ApplicationUncaughtExceptionHandler asyncExceptionHandler;

    @Bean
    ThreadPoolTaskSchedulerCustomizer schedulerCustomizer() {
        return taskScheduler ->
                taskScheduler.setErrorHandler(asyncExceptionHandler);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return asyncExceptionHandler;
    }

}


