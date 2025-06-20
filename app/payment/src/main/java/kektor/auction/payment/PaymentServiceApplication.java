package kektor.auction.payment;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PaymentServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(PaymentServiceApplication.class)
                .run(args);
    }
}
