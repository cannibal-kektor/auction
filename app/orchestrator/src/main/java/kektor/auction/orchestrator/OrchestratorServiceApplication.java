package kektor.auction.orchestrator;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class OrchestratorServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(OrchestratorServiceApplication.class)
                .run(args);
    }
}
