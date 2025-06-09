package kektor.auction.category.conf;


import kektor.auction.category.exception.handler.ApplicationUncaughtExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

@Configuration
//@EnableAsync
//@EnableScheduling
public class AsyncConfig implements AsyncConfigurer {

    @Autowired
    ApplicationUncaughtExceptionHandler exceptionHandler;

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

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return exceptionHandler;
    }

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


