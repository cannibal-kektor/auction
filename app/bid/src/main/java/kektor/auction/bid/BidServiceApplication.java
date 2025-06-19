package kektor.auction.bid;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BidServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(BidServiceApplication.class)
                .run(args);
    }
}
