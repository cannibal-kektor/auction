package kektor.auction.orchestrator.conf;

import kektor.auction.orchestrator.service.client.BidServiceClient;
import kektor.auction.orchestrator.service.client.LotServiceClient;
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
    public BidServiceClient bidServiceClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8082")
                .requestFactory(timeLimitedRequestFactory())
//                .defaultStatusHandler()
                .build();
        return createProxy(restClient, BidServiceClient.class);
    }

    @Bean
    public LotServiceClient lotServiceClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8081")
                .requestFactory(timeLimitedRequestFactory())
                .build();

        return createProxy(restClient, LotServiceClient.class);
    }


    private <T> T createProxy(RestClient restClient, Class<T> clazz) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(clazz);
    }

    JdkClientHttpRequestFactory timeLimitedRequestFactory() {
        var requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.of(5, SECONDS));
        return requestFactory;
    }

}
