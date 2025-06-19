package kektor.auction.sse;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SseServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SseServiceApplication.class)
                .run(args);
    }
}
