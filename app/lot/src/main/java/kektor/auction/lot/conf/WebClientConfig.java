package kektor.auction.lot.conf;

import kektor.auction.lot.service.client.CategoryServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

    @Bean
    public CategoryServiceClient categoryServiceClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
//                .defaultStatusHandler( )
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(CategoryServiceClient.class);
    }


}
