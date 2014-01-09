package eauction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class AuctionApplication {
  public static void main(String[] args) {
    SpringApplication.run(AuctionApplication.class, args);
  }
}
