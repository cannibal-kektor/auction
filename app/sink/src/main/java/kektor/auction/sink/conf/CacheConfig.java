package kektor.auction.sink.conf;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.TimeUnit;


@EnableCaching
@Configuration
public class CacheConfig {

    ThreadPoolTaskExecutor executor;

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .initialCapacity(100)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .executor(executor)
                .scheduler(Scheduler.systemScheduler());
    }
}
