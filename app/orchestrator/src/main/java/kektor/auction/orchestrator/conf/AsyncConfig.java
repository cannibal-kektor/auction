package kektor.auction.orchestrator.conf;


import kektor.auction.orchestrator.exception.handler.ApplicationUncaughtExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.task.ThreadPoolTaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer, SchedulingConfigurer {

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

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    }

}


