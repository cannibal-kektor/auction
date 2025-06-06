package kektor.auction.category.conf;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfig {
    //--------DataSourceAutoConfiguration-------------------------------------------------

//    spring:
//        datasource:
//        driver-class-name: org.postgresql.Driver #Auto-detected based on the URL by default
//        type: com.zaxxer.hikari.HikariDataSource #Fully qualified name of the connection pool implementation to use. By default, it is auto-detected from the classpath.
//        url: jdbc:postgresql://localhost:5432/dbtest
//        username: postgres
//        password: 13061306
//        jndi-name:
//        embedded-database-connection: NONE|H2|DERBY|HSQLDB
//        name:
//        generate-unique-name: true
//
//        hikari:
//            allow-pool-suspension:
//            auto-commit:
//            catalog:
//            connection-init-sql:
//            connection-test-query:
//            connection-timeout: 20000
//            data-source-class-name:
//            data-source-j-n-d-i:
//            data-source-properties:
//            driver-class-name:
//            exception-override-class-name:
//            health-check-properties:
//            idle-timeout:
//            initialization-fail-timeout:
//            isolate-internal-queries:
//            jdbc-url:
//            keepalive-time:
//            leak-detection-threshold:
//            login-timeout:
//            max-lifetime: 500000
//            maximum-pool-size: 3
//            metrics-tracker-factory:
//            minimum-idle:
//            password:
//            pool-name:
//            read-only:
//            register-mbeans:
//            scheduled-executor:
//            schema:
//            transaction-isolation:
//            username:
//            validation-timeout:

//    @Bean
//    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setJdbcUrl(dataSourceProperties.getUrl());
//        hikariConfig.setUsername(dataSourceProperties.getUsername());
//        hikariConfig.setPassword(dataSourceProperties.getPassword());
////        hikariConfig.setDriverClassName("org.postgresql.Driver");
//        hikariConfig.setConnectionTimeout(20000);
//        hikariConfig.setMaximumPoolSize(10);
//        hikariConfig.setMaxLifetime(900000);
//
//        return new HikariDataSource(hikariConfig);
//    }
}
