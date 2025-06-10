package kektor.auction.lot;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class LotServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(LotServiceApplication.class)
                .run(args);
    }
}
