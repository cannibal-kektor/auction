package kektor.auction.category;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CategoryServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(CategoryServiceApplication.class)
                .run(args);
    }
}
