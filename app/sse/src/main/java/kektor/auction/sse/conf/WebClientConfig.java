package kektor.auction.sse.conf;

import kektor.auction.sse.service.client.LotServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Configuration
public class WebClientConfig {

    @Bean
    public LotServiceClient lotServiceClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8081")
                .requestFactory(timeLimitedRequestFactory())
                .build();
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(LotServiceClient.class);
    }


    JdkClientHttpRequestFactory timeLimitedRequestFactory() {
        var requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.of(10, SECONDS));
        return requestFactory;
    }

}
