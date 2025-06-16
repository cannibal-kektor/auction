package kektor.auction.bid.conf;


import kektor.auction.bid.exception.handler.ApplicationUncaughtExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AsyncConfig implements AsyncConfigurer, SchedulingConfigurer {

    @Autowired
    ApplicationUncaughtExceptionHandler asyncExceptionHandler;

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

//    private final ThreadPoolTaskExecutor defaultSpringBootAsyncExecutor;
//    public AsyncConfig(ThreadPoolTaskExecutor defaultSpringBootAsyncExecutor) {
//        this.defaultSpringBootAsyncExecutor = defaultSpringBootAsyncExecutor;
//    }
//
//    @Override
//    public Executor getAsyncExecutor() {
//        defaultSpringBootAsyncExecutor.setTaskDecorator(loggingMDCTaskDecorator());
//        return new DelegatingSecurityContextAsyncTaskExecutor(defaultSpringBootAsyncExecutor);

//    }


//    @Bean
//    public DelegatingSecurityContextAsyncTaskExecutor taskExecutor(ThreadPoolTaskExecutor delegate) {
//        return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
//    }

//    @Bean
//    TaskDecorator composedTaskDecorator() {
//        return runnable -> securityTaskDecorator()
//                .andThen(loggingTaskDecorator())
//                .apply(runnable);
//    }
//
//    Function<Runnable, Runnable> loggingTaskDecorator() {
//        return runnable -> {
//            System.out.println("LOGGING SET------------------------");
//            Map<String, String> contextMap = MDC.getCopyOfContextMap();
//            return () -> {
//                try {
//                    MDC.setContextMap(contextMap);
//                    runnable.run();
//                } finally {
//                    MDC.clear();
//                }
//            };
//        };
//    }
//
//    Function<Runnable, Runnable> securityTaskDecorator() {
//        return delegate -> {
//            System.out.println("SECURITY APPLIED");
//            return new DelegatingSecurityContextRunnable(delegate);
//        };
//    }
}


