package kektor.auction.bid.conf;

import kektor.auction.bid.service.client.SagaOrchestratorClient;
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
    public SagaOrchestratorClient sagaOrchestratorClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8083")
                .requestFactory(timeLimitedRequestFactory())
                .build();
        return createProxy(restClient, SagaOrchestratorClient.class);
    }

    private <T> T createProxy(RestClient restClient, Class<T> clazz) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(clazz);
    }

    JdkClientHttpRequestFactory timeLimitedRequestFactory() {
        var requestFactory = new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.of(10, SECONDS));
        return requestFactory;
    }

}
