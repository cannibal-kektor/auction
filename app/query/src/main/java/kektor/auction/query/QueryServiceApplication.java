package kektor.auction.query;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class QueryServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(QueryServiceApplication.class)
                .run(args);
    }
}
