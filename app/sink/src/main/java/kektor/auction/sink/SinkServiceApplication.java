package kektor.auction.sink;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SinkServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SinkServiceApplication.class)
                .run(args);
    }
}
