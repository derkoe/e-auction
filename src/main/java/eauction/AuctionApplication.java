package eauction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import eauction.backend.AuctionInfoService;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class AuctionApplication {

  public static final Logger LOGGER = LoggerFactory.getLogger(AuctionInfoService.class);

  public static void main(String[] args) {
    SpringApplication.run(AuctionApplication.class, args);
  }
}
